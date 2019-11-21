package com.vas.desafioseniorsociotocedor.models;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Campanha extends RetryableMessage {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String nome;
	private long idTimeDoCoracao;
	private LocalDate dataFimVigencia;

}
