package com.vas.desafioseniorsociotocedor.broker;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vas.desafioseniorsociotocedor.dtos.CampanhaDTO;
import com.vas.desafioseniorsociotocedor.enums.CampanhaAction;
import com.vas.desafioseniorsociotocedor.services.CampanhaService;

@Service
public class CampanhaConsumer {

	@Autowired
	private CampanhaService campanhaService;
	@Autowired
	private ObjectMapper mapper;

	@KafkaListener(topics = "${spring.kafka.topic.campanha}", groupId = "${spring.kafka.groupid.campanha}")
	public void consume(@Payload String campanhaAsString, @Headers MessageHeaders headers) {
		try {
			CampanhaDTO campanhaDTO = mapper.readValue(campanhaAsString, CampanhaDTO.class);
			CampanhaAction action = CampanhaAction.valueOf(headers.get("action").toString());
			if (action == CampanhaAction.CREATED) {
				campanhaService.create(campanhaDTO);
			}
			if (action == CampanhaAction.UPDATED) {
				campanhaService.update(campanhaDTO);
			}
			if (action == CampanhaAction.DELETED) {
				campanhaService.delete(campanhaDTO);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
