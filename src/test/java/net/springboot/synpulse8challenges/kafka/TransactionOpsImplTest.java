package net.springboot.synpulse8challenges.kafka;

import lombok.extern.log4j.Log4j2;
import net.springboot.synpulse8challenges.config.ExternalUrlConfig;
import net.springboot.synpulse8challenges.config.KafkaTopicConfigs;
import net.springboot.synpulse8challenges.constants.TransactionConstants;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.model.Transaction;
import net.springboot.synpulse8challenges.model.TransactionQuery;
import net.springboot.synpulse8challenges.model.TransactionSummary;
import net.springboot.synpulse8challenges.repositories.TransactionRepositories;
import net.springboot.synpulse8challenges.utilities.DataProvider;
import net.springboot.synpulse8challenges.utilities.RestService;
import net.springboot.synpulse8challenges.utilities.ValidateUtility;
import org.apache.commons.lang3.StringUtils;
import org.bson.json.JsonObject;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
public class TransactionOpsImplTest {
    @Mock
    AccountOpsImpl accountOps;
    @Mock
    KafkaTopicConfigs kafkaTopicConfigs;
    @Mock
    KafkaTemplate<String, Transaction> transactionKafkaTemplate;
    @Mock
    TransactionRepositories transactionRepositories;
    @Mock
    ValidateUtility validateUtility;
    @Mock
    MongoTemplate mongoTemplate;
    @Mock
    ExternalUrlConfig externalUrlConfig;
    @Mock
    RestService restService;
    @InjectMocks
    TransactionOpsImpl transactionOps;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        when(kafkaTopicConfigs.getTransactionsTopic()).thenReturn("topic");
        when(externalUrlConfig.getExchangeRateUrl()).thenReturn("https://www.google.com");
    }

    @Test
    public void testCreateTransaction(){
        Transaction testData = DataProvider.prepareTransactionData(111d,"account","01-01-2022","description");
        when(validateUtility.validateTransaction(any())).thenReturn(Arrays.asList());
        when(accountOps.findAccountCurrency(any())).thenReturn("USD");

        ResponseEntity<ResponseObject> result = transactionOps.createTransaction(testData);
        Assertions.assertEquals(TransactionConstants.TRANSACTION_SUCCESSFUL,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.OK,result.getStatusCode());

        when(validateUtility.validateTransaction(any())).thenReturn(Arrays.asList("123"));
        result = transactionOps.createTransaction(testData);
        Assertions.assertEquals("123",result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
    }

    @Test
    public void testSendTransactionToKafka(){
        Transaction testData = DataProvider.prepareTransactionData(-111d,"account","01-01-2022","description");
        when(accountOps.findAccountCurrency(any())).thenReturn("USD");

        Transaction result = ReflectionTestUtils.invokeMethod(transactionOps,"sendTransactionToKafka",testData);

        Assertions.assertTrue(!StringUtils.isEmpty(result.getTransactionId()));
        Assertions.assertEquals("USD 111.0-",result.getCurrencyAmount());
        verify(transactionKafkaTemplate,times(1)).send(any(Message.class));
    }

    @Test
    public void testSaveTransactionFromTopic(){
        Transaction testData = DataProvider.prepareTransactionData(-111d,"account",
                "01-01-2022","description","USD 111-");

        transactionOps.saveTransactionFromTopic(testData);
        verify(transactionRepositories,times(1)).save(any());
    }

    @Test
    public void testFindTransactionSummaryByAccount(){
        when(validateUtility.validateTransactionQueries(any())).thenReturn(Arrays.asList("123"));
        TransactionQuery testData = new TransactionQuery();
        ResponseEntity<ResponseObject> result = transactionOps.findTransactionSummaryByAccount(testData);
        Assertions.assertEquals(Arrays.asList("123"),result.getBody().getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());

        Mockito.reset(validateUtility);
        testData = DataProvider.prepareTransactionQuery("01-01-2022","01-01-2022",
                "TRUE",null, 1,5);
        result = transactionOps.findTransactionSummaryByAccount(testData);
        Assertions.assertEquals(TransactionConstants.ERROR_QUERY_ACCOUNT_MISSING,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());

        testData = DataProvider.prepareTransactionQuery("01-01-2022","01-01-2022",
                "TRUE","TRUE", 1,5);
        when(mongoTemplate.find(any(Query.class),any(Class.class))).thenReturn(Arrays.asList());
        result = transactionOps.findTransactionSummaryByAccount(testData);
        Assertions.assertEquals(TransactionConstants.TRANSACTION_QUERY_NOT_FOUND,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.NOT_FOUND,result.getStatusCode());

        Mockito.reset(validateUtility);
        Transaction mockedTransaction1 = DataProvider.prepareTransactionData(-111d,"1","1","1");
        mockedTransaction1.setCurrency("GBP");
        Transaction mockedTransaction2 = DataProvider.prepareTransactionData(111d,"2","2","2");
        mockedTransaction2.setCurrency("JPY");
        Transaction mockedTransaction3 = DataProvider.prepareTransactionData(111d,"1","1","1");
        mockedTransaction1.setCurrency("GBP");
        List<Transaction> mockedTransactionList = Arrays.asList(
                mockedTransaction1, mockedTransaction2,mockedTransaction3);

        when(mongoTemplate.find(any(Query.class),any(Class.class))).thenReturn(mockedTransactionList);
        when(restService.callExternalAPI(any(),any(),any(),
                any(),any(Class.class))).thenReturn( prepareExchangeRateResponse("USD"));
        result = transactionOps.findTransactionSummaryByAccount(testData);
        Assertions.assertEquals(TransactionConstants.TRANSACTION_QUERY_SUCCESSFUL,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.OK,result.getStatusCode());
    }

    private String prepareExchangeRateResponse(String targetCurrency){
        JSONObject currencyObject = new JSONObject();
        JSONObject rateObject = new JSONObject();
        try{
            currencyObject.put(targetCurrency,1d);
            rateObject.put("rates",currencyObject);
        }catch(Exception ex){
            log.error("Fail to prepare exchangeRate Response",ex);
        }
        return rateObject.toString();
    }
}
