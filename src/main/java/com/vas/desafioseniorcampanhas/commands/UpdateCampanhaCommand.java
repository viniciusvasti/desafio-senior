package com.vas.desafioseniorcampanhas.commands;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Document
public class UpdateCampanhaCommand {
	
	@NotBlank
	private final String id;
	@NotBlank
	private final String nome;
	@NotNull
	private final LocalDate dataFimVigencia;

}
