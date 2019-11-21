package com.vas.desafioseniorsociotocedor.models;

import java.io.Serializable;
import java.util.Date;

public class Erro implements Serializable {

	private static final long serialVersionUID = 1L;
	private String topico;
	private Long offset;
	private Integer partition;
	private Date hora;
	private String stackTrace;
	private Object mensagem;

	public String getTopico() {
		return topico;
	}

	public void setTopico(String topico) {
		this.topico = topico;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public Object getMensagem() {
		return mensagem;
	}

	public void setMensagem(Object mensagem) {
		this.mensagem = mensagem;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Integer getPartition() {
		return partition;
	}

	public void setPartition(Integer partition) {
		this.partition = partition;
	}

	@Override
	public String toString() {
		return "Erro [topico=" + topico + ", offset=" + offset + ", partition=" + partition
				+ ", hora=" + hora + ", stackTrace=" + stackTrace + ", mensagem=" + mensagem + "]";
	}
}