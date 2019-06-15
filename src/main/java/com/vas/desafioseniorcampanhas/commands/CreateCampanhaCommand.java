package com.vas.desafioseniorcampanhas.commands;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateCampanhaCommand {
	
	@NotBlank
	private final String nome;
	@Positive
	private final long idTimeDoCoracao;
	@NotNull
	private final LocalDate dataFimVigencia;

}
