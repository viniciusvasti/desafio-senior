package com.vas.desafioseniorcampanhas.commands;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UpdateCampanhaCommand {
	
	@NotBlank
	private String id;
	@NotBlank
	private String nome;
	@NotNull
	private LocalDate dataFimVigencia;

}
