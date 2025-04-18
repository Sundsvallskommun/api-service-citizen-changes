package se.sundsvall.citizenchanges.integration.citizen.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration.citizen")
public record CitizenIntegrationProperties(int connectTimeout, int readTimeout) {
}
