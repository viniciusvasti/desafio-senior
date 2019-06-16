package com.vas.desafioseniorcampanhas.events;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CampanhaCreatedEventListener implements ApplicationListener<CampanhaEvent> {

	@Override
	public void onApplicationEvent(CampanhaEvent event) {
		System.out.println("Received CampanhaEvent - " + event.getAction().toString() + ": "
				+ event.getMessage());
	}

}
