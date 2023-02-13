package net.springboot.synpulse8challenges.kafka;

import lombok.extern.log4j.Log4j2;
import net.springboot.synpulse8challenges.model.Account;
import net.springboot.synpulse8challenges.model.Transaction;
import net.springboot.synpulse8challenges.model.UserCreation;
import net.springboot.synpulse8challenges.repositories.AccountRepositories;
import net.springboot.synpulse8challenges.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;


@Log4j2
@Service
public class KafkaListenerOps {
    @Autowired
    UserRepositories userRepositories;
    @Autowired
    AccountRepositories accountRepositories;
    @Autowired
    TransactionOpsImpl transactionOps;

    @KafkaListener(
            topicPartitions = @TopicPartition(topic = "${topic.config.users-topic}", partitions = {"${topic.config.partition}"}),
            containerFactory = "userCreationConcurrentKafkaListenerContainerFactory", id = "userTopicListener")
    public void consume(UserCreation userCreation) {
        log.info("Message receive from User Topic:{}", userCreation);
        userRepositories.save(userCreation);
    }

    @KafkaListener(
            topicPartitions = @TopicPartition(topic = "${topic.config.accounts-topic}", partitions = {"${topic.config.partition}"}),
            containerFactory = "accountConcurrentKafkaListenerContainerFactory", id = "accountTopicListener")
    public void consumeAccountTopic(Account account) {
        log.info("Message receive from Account Topic:{}", account);
        accountRepositories.save(account);
    }

    @KafkaListener(
            topicPartitions = @TopicPartition(topic = "${topic.config.transactions-topic}", partitions = {"${topic.config.partition}"}),
            containerFactory = "transactionConcurrentKafkaListenerContainerFactory", id = "transactionTopicListener")
    public void consumeTransactionTopic(Transaction transaction) {
        log.info("Message receive from Transaction Topic: {}", transaction);
        transactionOps.saveTransactionFromTopic(transaction);
    }
}
