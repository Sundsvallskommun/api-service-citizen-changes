package se.sundsvall.citizenchanges.integration.messaging;

import generated.se.sundsvall.messaging.EmailRequest;
import generated.se.sundsvall.messaging.MessageResult;
import generated.se.sundsvall.messaging.SmsRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import se.sundsvall.citizenchanges.integration.messaging.configuration.MessagingConfiguration;

import static se.sundsvall.citizenchanges.integration.messaging.configuration.MessagingConfiguration.CLIENT_ID;

@FeignClient(
	name = CLIENT_ID,
	url = "${integration.messaging.base-url}",
	configuration = MessagingConfiguration.class)
@CircuitBreaker(name = CLIENT_ID)
public interface MessagingClient {

	/**
	 * Send a single e-mail
	 *
	 * @param  municipalityId the municipality ID
	 * @param  request        containing email information
	 * @return                response containing id and delivery results for sent message
	 */
	@PostMapping("/{municipalityId}/email")
	MessageResult sendEmail(@PathVariable("municipalityId") String municipalityId, EmailRequest request);

	/**
	 * Send a single sms
	 *
	 * @param  municipalityId the municipality ID
	 * @param  request        containing sms information
	 * @return                response containing id and delivery results for sent message
	 */
	@PostMapping("/{municipalityId}/sms")
	MessageResult sendSms(@PathVariable("municipalityId") String municipalityId, SmsRequest request);
}
