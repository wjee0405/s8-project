package net.springboot.synpulse8challenges.kafka;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Configuration
public class TopicCreation {

    @Autowired
    KafkaAdmin kafkaAdmin;
    private static int partition = 1;
    private static int replica = 1;

    public void createTopic(String topicName){


        NewTopic newTopic =  TopicBuilder.name(topicName)
                .partitions(partition)
                .replicas(1)
                .build();
        kafkaAdmin.createOrModifyTopics(newTopic);
    }

}
