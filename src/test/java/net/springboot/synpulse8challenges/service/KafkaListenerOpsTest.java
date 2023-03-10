package net.springboot.synpulse8challenges.service;

import net.springboot.synpulse8challenges.model.Account;
import net.springboot.synpulse8challenges.model.Transaction;
import net.springboot.synpulse8challenges.model.User;
import net.springboot.synpulse8challenges.repositories.AccountRepositories;
import net.springboot.synpulse8challenges.repositories.UserRepositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class KafkaListenerOpsTest {
    @Mock
    UserRepositories userRepositories;
    @Mock
    AccountRepositories accountRepositories;
    @Mock
    TransactionOps transactionOps;
    @InjectMocks
    KafkaListenerOps kafkaListenerOps;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsume() {
        User user = new User();
        kafkaListenerOps.consume(user);
        verify(userRepositories, times(1)).save(any());
    }

    @Test
    public void testConsumeAccountTopic() {
        Account account = new Account();
        kafkaListenerOps.consumeAccountTopic(account);
        verify(accountRepositories, times(1)).save(any());
    }

    @Test
    public void testConsumeTransactionTopic() {
        Transaction transaction = new Transaction();
        kafkaListenerOps.consumeTransactionTopic(transaction);
        verify(transactionOps, times(1)).saveTransactionFromTopic(any());
    }
}
