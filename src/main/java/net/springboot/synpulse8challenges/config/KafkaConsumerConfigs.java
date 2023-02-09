package net.springboot.synpulse8challenges.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaConsumerConfigs {

    private String bootStrapServers;
    private String group;
    private String autoOffsetReset;
    private String keySerializer;
    private String valueDeserializer;
}
