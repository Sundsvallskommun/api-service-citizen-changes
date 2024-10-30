package se.sundsvall.citizenchanges.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mapper")
public record MessageMapperProperties(
	String recipientsSkolskjuts,
	String recipientsElevresa,
	String linkTemplate) {

}
