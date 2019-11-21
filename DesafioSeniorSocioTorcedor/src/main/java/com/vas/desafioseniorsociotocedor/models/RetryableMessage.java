package com.vas.desafioseniorsociotocedor.models;

import java.io.Serializable;

public class RetryableMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private int attempts = 0;

	private String topico;

	public int getAttempts() {
		return attempts;
	}

	public void addAttempt() {
		this.attempts++;
	}

	public String getTopico() {
		return topico;
	}

	public void setTopico(String topico) {
		this.topico = topico;
	}
}
