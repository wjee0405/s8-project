package net.springboot.synpulse8challenges.kafka;

import net.springboot.synpulse8challenges.config.KafkaConsumerConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAdminConfigs {

    @Autowired
    KafkaConsumerConfigs kafkaConsumerConfigs;

    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String,Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConsumerConfigs.getBootStrapServers());
        return new KafkaAdmin(configs);
    }
}
