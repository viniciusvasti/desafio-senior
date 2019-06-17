package com.vas.desafioseniorsociotocedor.models;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SocioTorcedor {
	@Id
	private String email;
	private String nome;
	private LocalDate dataNascimento;
	private long idTimeDoCoracao;
	@DBRef
	private Set<Campanha> campanhas;
}
