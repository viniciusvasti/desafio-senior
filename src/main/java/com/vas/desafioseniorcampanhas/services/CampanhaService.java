package com.vas.desafioseniorcampanhas.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vas.desafioseniorcampanhas.commands.CreateCampanhaCommand;
import com.vas.desafioseniorcampanhas.dtos.CampanhaDTO;

@Service
public class CampanhaService {

	public CampanhaDTO create(CreateCampanhaCommand command) {
		return new CampanhaDTO();
	}

	public List<CampanhaDTO> findAllVigentes() {
		return new ArrayList<>();
	}

}
