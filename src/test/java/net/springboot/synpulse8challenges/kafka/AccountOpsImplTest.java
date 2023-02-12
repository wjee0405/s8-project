package net.springboot.synpulse8challenges.kafka;

import net.springboot.synpulse8challenges.config.KafkaTopicConfigs;
import net.springboot.synpulse8challenges.constants.ResponseConstants;
import net.springboot.synpulse8challenges.model.Account;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.repositories.AccountRepositories;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import javax.xml.ws.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountOpsImplTest {
    @Mock
    KafkaTopicConfigs kafkaTopicConfigs;
    @Mock
    UserOps userOps;
    @Mock
    KafkaTemplate<String, Account> kafkaTemplate;
    @Mock
    AccountRepositories accountRepositories;
    @InjectMocks
    AccountOpsImpl accountsOps;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccount(){
        ResponseEntity<ResponseObject> result = accountsOps.createAccount("","");
        Assertions.assertEquals(ResponseConstants.INSUFFICIENT_INPUT_PARAMETER,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());

        result = accountsOps.createAccount("123","DUMMY_COUNTRY");
        Assertions.assertEquals(ResponseConstants.CURRENCY_NOT_SUPPORTED,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,result.getStatusCode());

        when(userOps.findUser(any())).thenReturn(Boolean.FALSE);
        result = accountsOps.createAccount("123","UNITED_KINGDOM");
        Assertions.assertEquals(ResponseConstants.USER_NOT_FOUND,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.NOT_FOUND,result.getStatusCode());

        when(userOps.findUser(any())).thenReturn(Boolean.TRUE);
        when(accountRepositories.findByAccountNo(any())).thenReturn(Optional.empty());
        result = accountsOps.createAccount("123","UNITED_KINGDOM");
        Assertions.assertEquals(ResponseConstants.ACCOUNT_CREATED,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.OK,result.getStatusCode());
        verify(kafkaTemplate,times(1)).send(any(Message.class));
    }

    @Test
    public void testFindAccountCurrency(){
        when(accountRepositories.findByAccountNo(any())).thenReturn(Optional.empty());
        String result = accountsOps.findAccountCurrency("123");
        Assertions.assertEquals(null,result);

        Account mockedAccount = new Account();
        mockedAccount.setCurrency("USD");
        when(accountRepositories.findByAccountNo(any())).thenReturn(Optional.of(mockedAccount));
        result = accountsOps.findAccountCurrency("123");
        Assertions.assertEquals("USD",result);
    }

    @Test
    public void testFindAllCurrencyAccounts(){
        List<Account> mockedAccountList = Arrays.asList(new Account());
        when(accountRepositories.findByAccountOwner(any())).thenReturn(mockedAccountList);
        List<Account> result = accountsOps.findAllCurrencyAccounts("123");
        Assertions.assertEquals(mockedAccountList,result);
    }

    @Test
    public void testFindCurrencyAccount(){
        ResponseEntity<ResponseObject> result = accountsOps.findCurrencyAccounts("");
        Assertions.assertEquals(ResponseConstants.INSUFFICIENT_INPUT_PARAMETER,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());

        when(userOps.findUser(any())).thenReturn(Boolean.FALSE);
        result = accountsOps.findCurrencyAccounts("123");
        Assertions.assertEquals(ResponseConstants.USER_NOT_FOUND,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());

        when(userOps.findUser(any())).thenReturn(Boolean.TRUE);
        when(accountRepositories.findByAccountOwner(any())).thenReturn(Arrays.asList());
        result = accountsOps.findCurrencyAccounts("123");
        Assertions.assertEquals(ResponseConstants.ACCOUNT_NOT_FOUND,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.NOT_FOUND,result.getStatusCode());

        when(accountRepositories.findByAccountOwner(any())).thenReturn(Arrays.asList(new Account()));
        result = accountsOps.findCurrencyAccounts("123");
        Assertions.assertEquals(ResponseConstants.ACCOUNT_FOUND,result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.FOUND,result.getStatusCode());
    }


}
