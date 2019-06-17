package com.vas.desafioseniorsociotocedor.broker;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.vas.desafioseniorsociotocedor.dtos.CampanhaEventDTO;

@Service
public class CampanhaConsumer {

	@KafkaListener(topics = "${spring.kafka.topic.campanha}", groupId = "${spring.kafka.groupid.campanha}")
	public void consume(CampanhaEventDTO campanhaEventDTO) {
		System.out.println(campanhaEventDTO);
	}

}
