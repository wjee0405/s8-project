package net.springboot.synpulse8challenges.kafka;

import net.springboot.synpulse8challenges.config.KafkaTopicConfigs;
import net.springboot.synpulse8challenges.constants.ResponseConstants;
import net.springboot.synpulse8challenges.model.Account;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.model.UserCreation;
import net.springboot.synpulse8challenges.repositories.UserRepositories;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserOpsTest {
    @Mock
    UserRepositories userRepositories;
    @Mock
    KafkaTopicConfigs kafkaTopicConfigs;
    @Mock
    KafkaTemplate<String, Account> kafkaTemplate;
    @InjectMocks
    UserOps userOps;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindUser() {
        when(userRepositories.findByUserId(any())).thenReturn(Optional.empty());
        boolean result = userOps.findUser("123");
        Assertions.assertFalse(result);

        UserCreation userCreation = new UserCreation();
        when(userRepositories.findByUserId(any())).thenReturn(Optional.of(userCreation));
        result = userOps.findUser("123");
        Assertions.assertTrue(result);
    }

    @Test
    public void testCreateUser() {
        ResponseEntity<ResponseObject> result = userOps.createUser("");
        Assertions.assertEquals(ResponseConstants.USERID_CANNOT_BE_NULL, result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        UserCreation userCreation = new UserCreation();
        when(userRepositories.findByUserId(any())).thenReturn(Optional.of(userCreation));
        result = userOps.createUser("123");
        Assertions.assertEquals(ResponseConstants.USER_EXISTS, result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        when(userRepositories.findByUserId(any())).thenReturn(Optional.empty());
        result = userOps.createUser("123");
        Assertions.assertEquals(ResponseConstants.USER_CREATION_SUCCESS, result.getBody().getMessage().get(0));
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(kafkaTemplate, times(1)).send(any(Message.class));
    }
}
