package com.vas.desafioseniorcampanhas.services;

import org.springframework.stereotype.Service;

import com.vas.desafioseniorcampanhas.commands.CreateCampanhaCommand;
import com.vas.desafioseniorcampanhas.dtos.CampanhaDTO;

@Service
public class CampanhaService {

	public CampanhaDTO create(CreateCampanhaCommand command) {

		return new CampanhaDTO();

	}

}
