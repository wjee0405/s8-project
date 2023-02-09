package net.springboot.synpulse8challenges.kafka;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.springboot.synpulse8challenges.model.Accounts;
import net.springboot.synpulse8challenges.model.ResponseObject;
import net.springboot.synpulse8challenges.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@NoArgsConstructor
public class TransactionOpsImpl {
    private KafkaTemplate<String, Transaction> transactionKafkaTemplate;

    public void createAccount(String userAccount,Accounts accountDetails){
        Message<Accounts> message = MessageBuilder.withPayload(accountDetails)
                .setHeader(KafkaHeaders.TOPIC,userAccount)
                .build();
        transactionKafkaTemplate.send(message);
    }

    private void sendTransaction(Transaction transaction,String account){
        Message<Transaction> message = MessageBuilder
                .withPayload(transaction)
                .setHeader(KafkaHeaders.TOPIC,account)
                .build();

        transactionKafkaTemplate.send(message);
    }
}
