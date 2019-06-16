package com.vas.desafioseniorcampanhas.commands;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateCampanhaCommand {
	
	@NotBlank
	private String nome;
	@Positive
	private long idTimeDoCoracao;
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull
	private LocalDate dataFimVigencia;

}
