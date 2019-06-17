package com.vas.desafioseniorsociotocedor.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailJaCadastradoException extends RuntimeException {

	public EmailJaCadastradoException(String message) {
		super(message);
	}

	public EmailJaCadastradoException(String message, Throwable cause) {
		super(message, cause);
	}

}