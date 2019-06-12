package com.vas.desafioseniorcampanhas.models;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Campanha {
	
	private String nome;
	private long idTimeDoCoracao;
	private Date dataVigencia;

}
