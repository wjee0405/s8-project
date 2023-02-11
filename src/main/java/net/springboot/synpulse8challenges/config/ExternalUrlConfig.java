package net.springboot.synpulse8challenges.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.url")
public class ExternalUrlConfig {
    private String exchangeRateUrl;
}
