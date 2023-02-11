package net.springboot.synpulse8challenges.kafka;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.springboot.synpulse8challenges.config.KafkaTopicConfigs;
import net.springboot.synpulse8challenges.constants.DateFormatConstants;
import net.springboot.synpulse8challenges.constants.TransactionConstants;
import net.springboot.synpulse8challenges.model.*;
import net.springboot.synpulse8challenges.repositories.TransactionRepositories;
import net.springboot.synpulse8challenges.utilities.DateUtility;
import net.springboot.synpulse8challenges.utilities.FinancialUtilities;
import net.springboot.synpulse8challenges.utilities.ResponseUtility;
import net.springboot.synpulse8challenges.utilities.ValidateUtility;
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
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@NoArgsConstructor
public class TransactionOpsImpl {

    @Autowired
    AccountOpsImpl accountOps;
    @Autowired
    KafkaTopicConfigs kafkaTopicConfigs;
    @Autowired
    KafkaTemplate<String, Transaction> transactionKafkaTemplate;
    @Autowired
    TransactionRepositories transactionRepositories;
    @Autowired
    ValidateUtility validateUtility;

    public ResponseEntity<ResponseObject> createTransaction(Transaction transaction){
        HttpStatus httpStatus = null;
        String transactionId = null;
        List<String> msg = new ArrayList<>();
        List<String> errorMessage = new ArrayList<>();
        errorMessage = validateUtility.validateTransaction(transaction);

        if(CollectionUtils.isEmpty(errorMessage)){
            sendTransactionToKafka(transaction);
            httpStatus = HttpStatus.OK;
            msg.add(TransactionConstants.TRANSACTION_SUCCESSFUL);
        }else{
            httpStatus = HttpStatus.BAD_REQUEST;
            msg = errorMessage;
        }
        return ResponseUtility.buildResponse(msg,httpStatus,transaction);
    }

    private Transaction sendTransactionToKafka(Transaction transaction){
        String uuid = UUID.randomUUID().toString();
        transaction.setTransactionId(uuid);

        String currencyAmount = accountOps.findAccountCurrency(transaction.getAccountNo()) + " " +
                Math.abs(transaction.getAmount()) +
                (FinancialUtilities.isAmountDebit(transaction.getAmount())?"-":"");
        transaction.setCurrencyAmount(currencyAmount);

        Message<Transaction> msg = MessageBuilder
                .withPayload(transaction)
                .setHeader(KafkaHeaders.TOPIC,kafkaTopicConfigs.getTransactionsTopic())
                .build();

        transactionKafkaTemplate.send(msg);
        return transaction;
    }

    public void saveTransactionFromTopic(Transaction transaction) {
        if (!StringUtils.isEmpty(transaction.getValueDate())) {
            transaction.setTransactionDate(
                    DateUtility.parseDateFromString(transaction.getValueDate(),
                            DateFormatConstants.DATE_FORMAT_DD_MM_YYYY));
        }
        try{
            if(!StringUtils.isEmpty(transaction.getCurrencyAmount())){
                String currency = transaction.getCurrencyAmount().substring(0,3);
                transaction.setCurrency(currency);

                String moneyValue = transaction.getCurrencyAmount().substring(4);
                Double amount = 0.0;
                if(moneyValue.endsWith("-")){
                    //Debit value
                    moneyValue = "-" + moneyValue.substring(0,
                            moneyValue.length()-1);
                    amount = Double.valueOf(moneyValue);
                }else{
                    amount = Double.valueOf(moneyValue);
                }
                transaction.setAmount(amount);
            }
        }catch(Exception ex){
            log.error("Fail to parse currency amount from topic",ex);
        }

        transactionRepositories.save(transaction);
    }

    public ResponseEntity<ResponseObject> findTransactionSummary(TransactionQuery transactionQuery){
        List<String> msg = new ArrayList<>();
        HttpStatus httpStatus = null;
        TransactionSummary transactionSummary = null;

        List<String> errorMessage = validateUtility.validateTransactionQueries(transactionQuery);

        
    }


}
