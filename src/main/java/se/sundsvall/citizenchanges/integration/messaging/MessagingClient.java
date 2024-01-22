package se.sundsvall.citizenchanges.integration.messaging;

import static se.sundsvall.citizenchanges.integration.messaging.configuration.MessagingConfiguration.CLIENT_ID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import se.sundsvall.citizenchanges.integration.messaging.configuration.MessagingConfiguration;

import generated.se.sundsvall.messaging.EmailRequest;
import generated.se.sundsvall.messaging.MessageResult;
import generated.se.sundsvall.messaging.SmsRequest;

@FeignClient(
	name = CLIENT_ID,
	url = "${integration.messaging.base-url}",
	configuration = MessagingConfiguration.class)
public interface MessagingClient {

	/**
	 * Send a single e-mail
	 *
	 * @param request containing email information
	 * @return response containing id and delivery results for sent message
	 */
	@PostMapping("/email")
	MessageResult sendEmail(EmailRequest request);

	/**
	 * Send a single sms
	 *
	 * @param request containing sms information
	 * @return response containing id and delivery results for sent message
	 */
	@PostMapping("/sms")
	MessageResult sendSms(SmsRequest request);

}
