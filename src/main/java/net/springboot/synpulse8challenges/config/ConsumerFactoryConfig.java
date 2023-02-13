package net.springboot.synpulse8challenges.config;

import net.springboot.synpulse8challenges.model.Account;
import net.springboot.synpulse8challenges.model.Transaction;
import net.springboot.synpulse8challenges.model.UserCreation;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class ConsumerFactoryConfig {
    @Autowired
    KafkaConsumerConfigs kafkaConsumerConfigs;

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaConsumerConfigs.getBootStrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                kafkaConsumerConfigs.getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                kafkaConsumerConfigs.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                kafkaConsumerConfigs.getValueDeserializer());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return props;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserCreation> userCreationConcurrentKafkaListenerContainerFactory() {
        Map<String, Object> configs = consumerConfigs();
        configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
                UserCreation.class);
        ConsumerFactory<String, UserCreation> consumerFactory = new DefaultKafkaConsumerFactory<>(configs);
        ConcurrentKafkaListenerContainerFactory<String, UserCreation> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Account> accountConcurrentKafkaListenerContainerFactory() {
        Map<String, Object> configs = consumerConfigs();
        configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
                Account.class);
        ConsumerFactory<String, Account> consumerFactory = new DefaultKafkaConsumerFactory<>(configs);
        ConcurrentKafkaListenerContainerFactory<String, Account> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Transaction> transactionConcurrentKafkaListenerContainerFactory() {
        Map<String, Object> configs = consumerConfigs();
        configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
                Transaction.class);
        ConsumerFactory<String, Transaction> consumerFactory = new DefaultKafkaConsumerFactory<>(configs);
        ConcurrentKafkaListenerContainerFactory<String, Transaction> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
