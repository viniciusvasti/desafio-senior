package com.vas.desafioseniorcampanhas.events;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.stereotype.Component;

@Component
public class CampanhaCreatedEventListener implements ApplicationListener<CampanhaEvent> {

	@Value(value = "${spring.kafka.topic.campanha}")
	private String topic;
	@Autowired
	private KafkaTemplate<String, CampanhaEvent> kafkaTemplate;

	@Override
	public void onApplicationEvent(CampanhaEvent event) {
		String message = "Received CampanhaEvent - " + event.getAction().toString() + ": "
				+ event.getCampanha();
		System.out.println(message);
		// TODO handle kafka unavailable
		try {
			kafkaTemplate.send(topic, event).get();
		} catch (TimeoutException | InterruptedException | ExecutionException ex) {
			throw new RuntimeException("Kafka timeout");
		}
	}

	@EventListener
	public void eventHandler(ListenerContainerIdleEvent event) {
		System.out.println("CAUGHT the event " + event);
	}

}
