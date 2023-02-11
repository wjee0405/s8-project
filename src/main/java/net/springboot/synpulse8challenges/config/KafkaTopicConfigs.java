package net.springboot.synpulse8challenges.config;

import lombok.Data;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Data
@Configuration
@ConfigurationProperties(prefix="topic.config")
public class KafkaTopicConfigs {
    private String usersTopic;
    private String accountsTopic;
    private String transactionsTopic;
    private int partition;

    @Bean
    public NewTopic createUserTopic(){
        return TopicBuilder.name(usersTopic)
                .partitions(partition)
                .build();
    }

    @Bean
    public NewTopic createAccountsTopic(){
        return TopicBuilder.name(accountsTopic)
                .partitions(partition)
                .build();
    }

    @Bean
    public NewTopic createTransactionsTopic(){
        return TopicBuilder.name(transactionsTopic)
                .partitions(partition)
                .build();
    }
}
