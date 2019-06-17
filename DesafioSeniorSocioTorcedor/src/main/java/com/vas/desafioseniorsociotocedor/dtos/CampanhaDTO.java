package com.vas.desafioseniorsociotocedor.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CampanhaDTO {
	
	private String id;
	private String nome;
	private long idTimeDoCoracao;
	@JsonSerialize(using = ToStringSerializer.class)
	private LocalDate dataFimVigencia;

}