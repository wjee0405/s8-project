package net.springboot.synpulse8challenges.kafka;

import lombok.extern.log4j.Log4j2;
import net.springboot.synpulse8challenges.config.KafkaConsumerConfigs;
import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import javax.annotation.PostConstruct;
import java.util.*;

@Log4j2
@Configuration
public class KafkaTopicOps {

    @Autowired
    KafkaConsumerConfigs kafkaConsumerConfigs;

    private KafkaAdmin kafkaAdmin;
    private AdminClient adminClient;
    private static int partition = 1;
    private static int replica = 1;

    @PostConstruct
    public void setUp(){
        Map<String,Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConsumerConfigs.getBootStrapServers());
        kafkaAdmin = new KafkaAdmin(configs);
        adminClient = AdminClient.create(configs);
    }

    public Set<String> getTopics(){
        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);
        Set<String> topicList = new HashSet<>();
        try{
            topicList = adminClient.listTopics(listTopicsOptions).names().get();
        }catch(Exception ex){
            log.error("Fail to retrieve topic lists",ex);
        }
        return topicList;
    }

    public boolean createTopic(String topicName){
        boolean result = Boolean.TRUE;

        Set<String> topicList = getTopics();
        if(topicList.contains(topicName)){
            result = Boolean.FALSE;
        }else{
            NewTopic newTopic =  TopicBuilder.name(topicName)
                    .partitions(partition)
                    .replicas(1)
                    .build();
            kafkaAdmin.createOrModifyTopics(newTopic);
            log.info("Created Topic:{}", topicName);
        }
        return result;

    }

}
