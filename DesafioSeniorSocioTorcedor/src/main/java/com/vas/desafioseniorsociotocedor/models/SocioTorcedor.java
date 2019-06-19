package com.vas.desafioseniorsociotocedor.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;

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
	@Email
	private String email;
	private String nome;
	private LocalDate dataNascimento;
	private long idTimeDoCoracao;
	@DBRef
	private Set<Campanha> campanhas;

	public SocioTorcedor(@Email String email, String nome, LocalDate dataNascimento,
			long idTimeDoCoracao) {
		super();
		this.email = email;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.idTimeDoCoracao = idTimeDoCoracao;
	}

	public Set<Campanha> getCampanhas() {
		if (campanhas == null)
			campanhas = new HashSet<>();
		return campanhas;
	}
}
