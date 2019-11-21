package com.vas.desafioseniorcampanhas.events;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vas.desafioseniorcampanhas.exceptions.KafkaProducerCampanhaException;

@Component
public class CampanhaEventListener implements ApplicationListener<CampanhaEvent> {

	@Value(value = "${spring.kafka.topics.campanha}")
	private String topic;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	private ObjectMapper mapper;

	@Override
	public void onApplicationEvent(CampanhaEvent event) {
		try {
			Message<String> message = MessageBuilder
					.withPayload(mapper.writeValueAsString(event.getCampanha()))
					.setHeader(KafkaHeaders.TOPIC, topic)
					.setHeader("action", event.getAction().toString())
					.build();
			kafkaTemplate.send(message).get();
		} catch (TimeoutException | InterruptedException | ExecutionException ex) {
			throw new KafkaProducerCampanhaException("Kafka timeout");
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Kafka json deserialize error");
		}
	}

	@EventListener
	public void eventHandler(ListenerContainerIdleEvent event) {
		System.out.println("CAUGHT the event " + event);
	}

}
