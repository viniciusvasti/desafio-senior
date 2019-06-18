package com.vas.desafioseniorcampanhas.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.vas.desafioseniorcampanhas.dtos.CampanhaDTO;
import com.vas.desafioseniorcampanhas.enums.CampanhaAction;

@Component
public class CampanhaEventPublisher {
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public void publish(final CampanhaDTO message, final CampanhaAction action) {
		System.out.println("Publishing campanhaCreatedEvent. ");
		CampanhaEvent campanhaCreatedEvent = new CampanhaEvent(this, message, action);
		applicationEventPublisher.publishEvent(campanhaCreatedEvent);
	}
}
