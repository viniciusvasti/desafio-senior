package com.vas.desafioseniorcampanhas.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.core.KafkaTemplate;
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
		kafkaTemplate.send(topic, event);
	}

}
