package net.springboot.synpulse8challenges.config;

import net.springboot.synpulse8challenges.model.Account;
import net.springboot.synpulse8challenges.model.Transaction;
import net.springboot.synpulse8challenges.model.User;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerFactoryConfig {
    @Autowired
    KafkaProducerConfigs kafkaProducerConfigs;

    private Map<String, Object> producerConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaProducerConfigs.getBootStrapServers());
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                kafkaProducerConfigs.getKeySerializer());
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                kafkaProducerConfigs.getValueSerializer());
        return configProps;
    }

    @Bean
    public KafkaTemplate<String, Account> accountsKafkaTemplate() {
        ProducerFactory<String, Account> producerFactory = new DefaultKafkaProducerFactory<>(producerConfig());
        KafkaTemplate<String, Account> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, User> userKafkaTemplate() {
        ProducerFactory<String, User> producerFactory = new DefaultKafkaProducerFactory<>(producerConfig());
        KafkaTemplate<String, User> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, Transaction> transactionKafkaTemplate() {
        ProducerFactory<String, Transaction> producerFactory = new DefaultKafkaProducerFactory<>(producerConfig());
        KafkaTemplate<String, Transaction> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        return kafkaTemplate;
    }

}
