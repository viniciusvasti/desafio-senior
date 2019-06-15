package com.vas.desafioseniorcampanhas.dtos;

import java.time.LocalDate;

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
public class CampanhaDTO {
	
	private String id;
	private String nome;
	private long idTimeDoCoracao;
	private LocalDate dataFimVigencia;

}
