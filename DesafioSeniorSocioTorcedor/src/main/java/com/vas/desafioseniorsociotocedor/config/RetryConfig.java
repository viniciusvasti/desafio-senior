package com.vas.desafioseniorsociotocedor.config;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vas.desafioseniorsociotocedor.models.Erro;
import com.vas.desafioseniorsociotocedor.models.RetryableMessage;

@Component
public class RetryConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(RetryConfig.class);

	@Value("${spring.kafka.retry.initial.interval.time.ms}")
	public Integer retryPolicyInitialInterval;

	@Value("${spring.kafka.retry.multiplier}")
	public Integer retryPolicyMultiplier;

	@Value("${spring.kafka.consume.attempts}")
	public Integer retryPolicyMaxAttempts;

	@Value("${spring.kafka.retry.max.interval}")
	public Integer retryPolicyMaxInterval;

	@Value("${spring.kafka.retry.multiplier}")
	public Integer multiplier;

	@Value("${spring.kafka.retry.quantidade.retencao}")
	private Integer quantidadeRetencao;

	@Value("${spring.kafka.topics.erro}")
	private String topicoErro;

	@Value("${spring.kafka.topics.retencao}")
	private String topicoRetencaoMensagens;

	@Autowired
	private KafkaTemplate<String, Object> producer;
	@Autowired
	private ObjectMapper mapper;

	public RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();

		ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
		exponentialBackOffPolicy.setInitialInterval(retryPolicyInitialInterval);
		exponentialBackOffPolicy.setMultiplier(retryPolicyMultiplier);
		exponentialBackOffPolicy.setMaxInterval(retryPolicyMaxInterval);
		retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(retryPolicyMaxAttempts);
		retryTemplate.setRetryPolicy(retryPolicy);

		return retryTemplate;
	}

	public RecoveryCallback<RetryTemplate> recoveryCallback(RetryContext retryContext)
			throws JsonParseException, JsonMappingException, IOException {
		LOGGER.info(
				"############################ Esgotou os retries, vai pro topico de retencao ou erro");
		ConsumerRecord<?, ?> record = ((ConsumerRecord<?, ?>) retryContext.getAttribute("record"));

		RetryableMessage receivedMessage;
		if (record.value() != null)
			receivedMessage = mapper.readValue(record.value().toString(), RetryableMessage.class);
		else
			return null;
		LOGGER.error(
				"Erro {} - Limite de Retries atingido. Enviando para tópico de retenção ou erro: {}, Erro: {}",
				receivedMessage.getTopico(),
				record.value(),
				retryContext.getLastThrowable());

		if (true && receivedMessage.getAttempts() >= quantidadeRetencao) {
			LOGGER.info(
					"############################ Esgotou as retenções, vai pro topico de erro");
			Erro erro = null;
			try {
				erro = new Erro();

				erro.setTopico(record.topic());
				erro.setOffset(record.offset());
				erro.setPartition(record.partition());
				erro.setHora(Date.from(Instant.ofEpochMilli(record.timestamp())));
				erro.setStackTrace(retryContext.getLastThrowable().getMessage());
				erro.setMensagem(record.value());
				producer.send(topicoErro, erro).get();
			} catch (Exception e) {
				LOGGER.error("Erro ao incluir na fila de erro: {} - Exception: {}", erro, e);
			}
		} else {
			LOGGER.info("############################ Vai pro topico de retencao");
			try {
				receivedMessage.addAttempt();
				receivedMessage.setTopico(record.topic());
				MessageHeaderAccessor accessor = new MessageHeaderAccessor();
				record.headers().forEach(h -> accessor.setHeader(h.key(), h.value()));
				Message<String> message = MessageBuilder
						.withPayload(mapper.writeValueAsString(receivedMessage))
						.setHeaders(accessor)
						.build();
				producer.send(topicoRetencaoMensagens, mapper.writeValueAsString(message))
						.get();
			} catch (Exception e) {
				LOGGER.error("Erro ao incluir na fila de retencao - Exception: {}", e);
			}
		}
		return null;
	}
}
