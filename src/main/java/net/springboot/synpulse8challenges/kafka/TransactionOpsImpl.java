package net.springboot.synpulse8challenges.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.springboot.synpulse8challenges.config.ExternalUrlConfig;
import net.springboot.synpulse8challenges.config.KafkaTopicConfigs;
import net.springboot.synpulse8challenges.constants.DateFormatConstants;
import net.springboot.synpulse8challenges.constants.TransactionConstants;
import net.springboot.synpulse8challenges.model.*;
import net.springboot.synpulse8challenges.repositories.TransactionRepositories;
import net.springboot.synpulse8challenges.utilities.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.ws.Response;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@NoArgsConstructor
public class TransactionOpsImpl {
    @Autowired
    UserOps userOps;
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
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ExternalUrlConfig externalUrlConfig;
    @Autowired
    RestService restService;


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

    public ResponseEntity<ResponseObject> findTransactionSummaryByAccount(TransactionQuery transactionQuery){
        List<String> msg = new ArrayList<>();
        HttpStatus httpStatus;
        TransactionSummary transactionSummary = new TransactionSummary();

        List<String> errorMessage = validateUtility.validateTransactionQueries(transactionQuery);
        List<Transaction> transactionList = new ArrayList<>();
        int pageIndex = !ObjectUtils.isEmpty(transactionQuery.getPageIndex())?transactionQuery.getPageIndex():0;
        int pageRecordCount = !ObjectUtils.isEmpty(transactionQuery.getPageRecordCount())?transactionQuery.getPageRecordCount():1;

        if(CollectionUtils.isEmpty(errorMessage)) {
            String accountNo = transactionQuery.getAccountNo();
            String startDateValue = transactionQuery.getTransactionStartDateValue();
            String endDateValue = transactionQuery.getTransactionEndDateValue();

            if(StringUtils.isEmpty(accountNo)){
                msg.add(TransactionConstants.ERROR_QUERY_ACCOUNT_MISSING);
                httpStatus = HttpStatus.BAD_REQUEST;
            }else{
                Query query = new Query();
                query.addCriteria(Criteria.where(TransactionConstants.ACCOUNT_NO).is(accountNo));
                if(!StringUtils.isEmpty(startDateValue)){
                    Date startDate = DateUtility.parseDateFromString(startDateValue,
                            DateFormatConstants.DATE_FORMAT_DD_MM_YYYY);
                    Date endDate = DateUtility.parseDateFromString(endDateValue,
                            DateFormatConstants.DATE_FORMAT_DD_MM_YYYY);
                    Criteria criteria = new Criteria();
                    Criteria startDateCriteria = Criteria.where(TransactionConstants.TRANSACTION_DATE).gte(startDate);
                    Criteria endDateCriteria = Criteria.where(TransactionConstants.TRANSACTION_DATE).lte(endDate);

                    criteria.andOperator(Arrays.asList(startDateCriteria,endDateCriteria));
                    query.addCriteria(criteria);
                }

                Pageable pageable = PageRequest.of(pageIndex, pageRecordCount, Sort.Direction.ASC, TransactionConstants.TRANSACTION_DATE);
                query.with(pageable);

                transactionList = mongoTemplate.find(query,Transaction.class);
                if(!CollectionUtils.isEmpty(transactionList)){
                    transactionSummary = calculateTransactionSummary(transactionList);
                    transactionSummary.setPageIndex(pageIndex);
                    transactionSummary.setPageRecordCount(pageRecordCount);
                    msg.add(TransactionConstants.TRANSACTION_QUERY_SUCCESSFUL);
                    httpStatus = HttpStatus.OK;
                }else{
                    msg.add(TransactionConstants.TRANSACTION_QUERY_NOT_FOUND);
                    httpStatus = HttpStatus.NOT_FOUND;
                }
            }
        }else{
            msg = errorMessage;
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return ResponseUtility.buildResponse(msg,httpStatus,transactionSummary);

    }

    public ResponseEntity<ResponseObject> findTransactionSummaryByUser(TransactionQuery transactionQuery){
        List<String> msg = new ArrayList<>();
        HttpStatus httpStatus;
        TransactionSummary transactionSummary = new TransactionSummary();

        List<String> errorMessage = validateUtility.validateTransactionQueries(transactionQuery);
        List<Transaction> transactionList = new ArrayList<>();
        int pageIndex = !ObjectUtils.isEmpty(transactionQuery.getPageIndex())?transactionQuery.getPageIndex():0;
        int pageRecordCount = !ObjectUtils.isEmpty(transactionQuery.getPageRecordCount())?transactionQuery.getPageRecordCount():100;

        if(CollectionUtils.isEmpty(errorMessage)){
            String userID = transactionQuery.getUserId();
            String startDateValue = transactionQuery.getTransactionStartDateValue();
            String endDateValue = transactionQuery.getTransactionEndDateValue();
            if(StringUtils.isEmpty(userID)){
                msg.add(TransactionConstants.ERROR_QUERY_USERID_MISSING);
                httpStatus = HttpStatus.BAD_REQUEST;
            }else{
                List<Account> accounts = accountOps.findAllCurrencyAccounts(userID);
                if(CollectionUtils.isEmpty(accounts)){
                    msg.add(TransactionConstants.ERROR_QUERY_USER_ACCOUNT_NOT_FOUND);
                    httpStatus = HttpStatus.BAD_REQUEST;
                }else{
                    Query query = new Query();
                    List<String> accountNos = accounts.stream().
                            map(p -> p.getAccountNo()).collect(Collectors.toList());
                    query.addCriteria(Criteria.where(TransactionConstants.ACCOUNT_NO).in(accountNos));
                    if(!StringUtils.isEmpty(startDateValue)){
                        Date startDate = DateUtility.parseDateFromString(startDateValue,
                                DateFormatConstants.DATE_FORMAT_DD_MM_YYYY);
                        Date endDate = DateUtility.parseDateFromString(endDateValue,
                                DateFormatConstants.DATE_FORMAT_DD_MM_YYYY);
                        Criteria criteria = new Criteria();
                        Criteria startDateCriteria = Criteria.where(TransactionConstants.TRANSACTION_DATE).gte(startDate);
                        Criteria endDateCriteria = Criteria.where(TransactionConstants.TRANSACTION_DATE).lte(endDate);

                        criteria.andOperator(Arrays.asList(startDateCriteria,endDateCriteria));
                        query.addCriteria(criteria);
                    }

                    Pageable pageable = PageRequest.of(pageIndex, pageRecordCount, Sort.Direction.ASC, TransactionConstants.TRANSACTION_DATE);
                    query.with(pageable);

                    transactionList = mongoTemplate.find(query,Transaction.class);
                    if(!CollectionUtils.isEmpty(transactionList)){
                        transactionSummary = calculateTransactionSummary(transactionList);
                        transactionSummary.setPageIndex(pageIndex);
                        transactionSummary.setPageRecordCount(pageRecordCount);
                        msg.add(TransactionConstants.TRANSACTION_QUERY_SUCCESSFUL);
                        httpStatus = HttpStatus.OK;
                    }else{
                        msg.add(TransactionConstants.TRANSACTION_QUERY_NOT_FOUND);
                        httpStatus = HttpStatus.NOT_FOUND;
                    }
                }
            }
        }else{
            msg = errorMessage;
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return ResponseUtility.buildResponse(msg,httpStatus,transactionSummary);
    }

    public TransactionSummary calculateTransactionSummary(List<Transaction> transactionList) {
        TransactionSummary transactionSummary = new TransactionSummary();
        //Transaction which are credit

        transactionSummary.setTransactionList(transactionList);

        List<Transaction> creditTransactions = transactionList.stream()
                .filter(t -> (t.getAmount() >= 0.0)).collect(Collectors.toList());

        //Transaction which are debit
        List<Transaction> debitTransactions = transactionList.stream()
                .filter(t -> (t.getAmount() < 0.0)).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(creditTransactions)) {
            Map<String, Double> creditTotal = compileCurrencyTotal(creditTransactions);

            Double creditValue = calculateTotalExchangeRate(creditTotal);
            transactionSummary.setCredit(creditValue);
            transactionSummary.setCreditInOriginalCurrency(creditTotal);
        }
        if(!CollectionUtils.isEmpty(debitTransactions)){
            Map<String, Double> debitTotal = compileCurrencyTotal(debitTransactions);

            Double debitValue = calculateTotalExchangeRate(debitTotal);
            transactionSummary.setDebit(debitValue);
            transactionSummary.setDebitInOriginalCurrency(debitTotal);
        }

        transactionSummary.setExchangedCurrency("USD");
        return transactionSummary;
    }

    public Map<String, Double> compileCurrencyTotal(List<Transaction> transactionList){
        Map<String, Double> currencyTotal = new HashMap<>();
        for(Transaction transaction:transactionList){
            if(!currencyTotal.containsKey(transaction.getCurrency())){
                currencyTotal.putIfAbsent(transaction.getCurrency(),
                        Math.abs(transaction.getAmount()));
            }else{
                Double totalAmount = currencyTotal.get(transaction.getCurrency()) + Math.abs(transaction.getAmount());
                currencyTotal.put(transaction.getCurrency(),
                        totalAmount);
            }
        }
        return currencyTotal;
    }

    public Double calculateTotalExchangeRate(Map<String, Double> currencyTotal){
        Double result = 0.0;

        for( Map.Entry<String,Double> entry : currencyTotal.entrySet()){
            Double exchangeRateForCurrency = getExchangeRate(entry.getKey(),"USD");
            Double exchangeValue = entry.getValue() * exchangeRateForCurrency;
            result += exchangeValue;
        }

        return result;
    }

    public Double getExchangeRate(String base,String targetCurrency){
        Double result = 0.0;
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(externalUrlConfig.getExchangeRateUrl())
                .queryParam("base",base)
                .queryParam("symbols",targetCurrency);
        log.info("Calling exchange rate API");
        String exchangeRateResponse = (String) restService.callExternalAPI(uri.toUriString(),null,null, HttpMethod.GET,String.class);
        if(!ObjectUtils.isEmpty(exchangeRateResponse)){
            ObjectMapper mapper = new ObjectMapper();
            try{
                JsonNode map = mapper.readTree(exchangeRateResponse);
                JsonNode rates = map.get("rates");
                if(!ObjectUtils.isEmpty(rates)){
                    result = rates.get(targetCurrency).asDouble();
                    log.info("Rate for {} from {} is {} on {}",targetCurrency,base,result, LocalDate.now());
                }
            }catch(Exception ex){
                log.error("Fail to parse exchange rate",ex);
            }
        }
        return result;
    }
}
