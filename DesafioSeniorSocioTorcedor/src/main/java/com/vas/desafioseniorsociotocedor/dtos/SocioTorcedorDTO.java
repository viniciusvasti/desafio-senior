package com.vas.desafioseniorsociotocedor.dtos;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SocioTorcedorDTO {
	@Email
	private String email;
	private String nome;
	private LocalDate dataNascimento;
	private long idTimeDoCoracao;
	@DBRef
	private Set<CampanhaDTO> campanhas;

	public SocioTorcedorDTO(@Email String email, String nome, LocalDate dataNascimento,
			long idTimeDoCoracao) {
		super();
		this.email = email;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.idTimeDoCoracao = idTimeDoCoracao;
	}

	public Set<CampanhaDTO> getCampanhas() {
		if (campanhas == null)
			campanhas = new HashSet<>();
		return campanhas;
	}
}
