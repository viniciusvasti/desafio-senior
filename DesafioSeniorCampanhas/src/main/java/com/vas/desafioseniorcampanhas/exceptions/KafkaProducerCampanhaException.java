package com.vas.desafioseniorcampanhas.exceptions;

@SuppressWarnings("serial")
public class KafkaProducerCampanhaException extends RuntimeException {

	public KafkaProducerCampanhaException(String message) {
		super(message);
	}

	public KafkaProducerCampanhaException(String message, Throwable cause) {
		super(message, cause);
	}

}