package com.vas.desafioseniorsociotocedor.dtos;

import com.vas.desafioseniorsociotocedor.enums.CampanhaAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CampanhaEventDTO {
	private CampanhaDTO message;
	private CampanhaAction action;
}
