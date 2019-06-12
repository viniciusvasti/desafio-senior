package com.vas.desafioseniorcampanhas.models;

import java.sql.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class Campanha {
	
	@Id
	private String id;
	private String nome;
	private long idTimeDoCoracao;
	private Date dataVigencia;

}
