package se.sundsvall.citizenchanges.integration.messaging.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("integration.messaging")
public record MessagingProperties(int connectTimeout, int readTimeout) {
}
