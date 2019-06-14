package com.vas.desafioseniorcampanhas.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vas.desafioseniorcampanhas.commands.CreateCampanhaCommand;
import com.vas.desafioseniorcampanhas.dtos.CampanhaDTO;
import com.vas.desafioseniorcampanhas.services.CampanhaService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/campanhas")
public class CampanhaController {

	private final CampanhaService campanhaService;

	@PostMapping
	public ResponseEntity<CampanhaDTO> post(@RequestBody CreateCampanhaCommand command) {
		CampanhaDTO dto = campanhaService.create(command);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(location).body(dto);
	}

	@GetMapping
	public ResponseEntity<List<CampanhaDTO>> get() {
		return new ResponseEntity<>(campanhaService.findAllVigentes(),
				HttpStatus.OK);
	}

}
