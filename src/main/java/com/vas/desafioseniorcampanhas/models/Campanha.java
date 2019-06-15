package com.vas.desafioseniorcampanhas.models;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class Campanha {
	
	@Id
	private String id;
	private String nome;
	private long idTimeDoCoracao;
	private LocalDate dataFimVigencia;

}
