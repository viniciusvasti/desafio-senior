package com.vas.desafioseniorcampanhas.events;

import org.springframework.context.ApplicationEvent;

import com.vas.desafioseniorcampanhas.dtos.CampanhaDTO;
import com.vas.desafioseniorcampanhas.enums.CampanhaAction;

@SuppressWarnings("serial")
public class CampanhaEvent extends ApplicationEvent {
	private final CampanhaDTO campanha;
	private final CampanhaAction action;

	public CampanhaEvent(Object source, CampanhaDTO message, CampanhaAction action) {
		super(source);
		this.campanha = message;
		this.action = action;
	}

	public CampanhaDTO getCampanha() {
		return campanha;
	}

	public CampanhaAction getAction() {
		return action;
	}
}
