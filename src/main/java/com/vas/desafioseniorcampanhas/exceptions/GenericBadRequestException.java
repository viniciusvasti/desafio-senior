package com.vas.desafioseniorcampanhas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GenericBadRequestException extends RuntimeException {

	public GenericBadRequestException(String message) {
		super(message);
	}

	public GenericBadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

}