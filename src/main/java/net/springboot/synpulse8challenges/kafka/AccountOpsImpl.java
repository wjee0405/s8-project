package net.springboot.synpulse8challenges.kafka;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.springboot.synpulse8challenges.config.KafkaTopicConfigs;
import net.springboot.synpulse8challenges.constants.ResponseConstants;
import net.springboot.synpulse8challenges.model.Account;
import net.springboot.synpulse8challenges.model.Currency;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.repositories.AccountRepositories;
import net.springboot.synpulse8challenges.utilities.ResponseUtility;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.*;

@Service
@NoArgsConstructor
@Log4j2
public class AccountOpsImpl {
    @Autowired
    KafkaTopicConfigs kafkaTopicConfigs;
    @Autowired
    UserOps userOps;
    @Autowired
    private KafkaTemplate<String, Account> accountsKafkaTemplate;
    @Autowired
    AccountRepositories accountRepositories;

    public ResponseEntity<ResponseObject> createAccount(String userId,String country){
        List<String> msg=new ArrayList<>();
        HttpStatus httpStatus = null;
        Account account = null;
        if(StringUtils.isEmpty((userId)) || StringUtils.isEmpty(country)){
            msg.add(ResponseConstants.INSUFFICIENT_INPUT_PARAMETER);
            httpStatus = HttpStatus.BAD_REQUEST;
        }else{
            if(EnumUtils.isValidEnum(Currency.class,country)){
                if(userOps.findUser(userId)){
                    account = createAccountInCurrency(userId,country);
                    msg.add(ResponseConstants.ACCOUNT_CREATED);
                    httpStatus = HttpStatus.OK;
                }else{
                    msg.add(ResponseConstants.USER_NOT_FOUND);
                    httpStatus = HttpStatus.NOT_FOUND;
                }
            }else{
                msg.add(ResponseConstants.CURRENCY_NOT_SUPPORTED);
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return ResponseUtility.buildResponse(msg,httpStatus,account);
    }

    public Account createAccountInCurrency(String userId, String country){
        String newAccountId = generateNewAccount(country);
        //Generate new account if same one is found
        while(findCurrencyAccount(newAccountId)){
            newAccountId = generateNewAccount(country);
        }
        Account account = new Account();
        account.setAccountNo(newAccountId);
        account.setAccountOwner(userId);
        account.setCountry(country);
        account.setCurrency((Currency.valueOf(country).getCurrency()));
        account.setActive(Boolean.TRUE);

        Message<Account> msg = MessageBuilder.withPayload(account)
                .setHeader(KafkaHeaders.TOPIC,kafkaTopicConfigs.getAccountsTopic())
                        .build();
        accountsKafkaTemplate.send(msg);

        return account;
    }

    private String generateNewAccount(String country){
        Random rand = new Random();
        String tempAccount = Currency.valueOf(country).getAccountPrefix();
        String accountNo ="";
        //Generate 14 numbers for account
        for(int i=0;i<14;i++){
            int n = rand.nextInt(10)+0;
            tempAccount += Integer.toString(n);
        }
        for(int i =0 ; i<tempAccount.length();i++){
            if(i % 4 == 0 && i != 0){
                accountNo += "-";
            }
            accountNo += tempAccount.charAt(i);
        }

        return accountNo;
    }

    public boolean findCurrencyAccount(String accountId) {
        Optional<Account> account = accountRepositories.findByAccountNo(accountId);
        return(account.isPresent());
    }

    public String findAccountCurrency(String accountId){
        String currency = null;
        Optional<Account> account = accountRepositories.findByAccountNo(accountId);
        if(account.isPresent()){
            currency = account.get().getCurrency();
        };

        return currency;
    }

    public List<Account> findAllCurrencyAccounts(String userId){
        return accountRepositories.findByAccountOwner(userId);
    }

    public ResponseEntity<ResponseObject> findCurrencyAccounts(String userId){
        List<String> msg=new ArrayList<>();
        List<Account> accounts = new ArrayList<>();
        HttpStatus httpStatus = null;

        if(StringUtils.isEmpty(userId)){
            msg.add(ResponseConstants.INSUFFICIENT_INPUT_PARAMETER);
            httpStatus = HttpStatus.BAD_REQUEST;
        }else{
            if(userOps.findUser(userId)){
                accounts = findAllCurrencyAccounts(userId);
                if(CollectionUtils.isEmpty(accounts)){
                    httpStatus = HttpStatus.NOT_FOUND;
                    msg.add(ResponseConstants.ACCOUNT_NOT_FOUND);
                }else{
                    httpStatus = HttpStatus.FOUND;
                    msg.add(ResponseConstants.ACCOUNT_FOUND);
                }
            }else{
                msg.add(ResponseConstants.USER_NOT_FOUND);
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        }

        return ResponseUtility.buildResponse(msg,httpStatus,accounts);
    }

}
