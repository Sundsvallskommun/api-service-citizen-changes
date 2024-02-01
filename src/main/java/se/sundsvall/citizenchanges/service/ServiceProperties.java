package se.sundsvall.citizenchanges.service;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "service")
public record ServiceProperties(String familyId) {
}
