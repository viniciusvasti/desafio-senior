package com.vas.desafioseniorcampanhas.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vas.desafioseniorcampanhas.commands.CreateCampanhaCommand;
import com.vas.desafioseniorcampanhas.commands.UpdateCampanhaCommand;
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
	public ResponseEntity<List<CampanhaDTO>> getAll() {
		return ResponseEntity.ok(campanhaService.findAll());
	}

	@GetMapping("/vigentes")
	public ResponseEntity<List<CampanhaDTO>> getVigentes() {
		return ResponseEntity.ok(campanhaService.findAllVigentes());
	}

	@PatchMapping("/{id}")
	public ResponseEntity<CampanhaDTO> patch(@PathVariable String id,
			@RequestBody UpdateCampanhaCommand command) {
		command.setId(id);
		CampanhaDTO dto = campanhaService.update(command);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable String id) {
		campanhaService.deleteById(id);
		return ResponseEntity.ok("Campanha excluída com sucesso.");
	}

}
