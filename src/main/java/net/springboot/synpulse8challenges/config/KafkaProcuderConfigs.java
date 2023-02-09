package net.springboot.synpulse8challenges.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="spring.kafka.producer")
public class KafkaProcuderConfigs {
    private String bootStrapServers;
    private String keySerializer;
    private String valueDeserializer;

}
