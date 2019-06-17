package com.vas.desafioseniorsociotocedor.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vas.desafioseniorsociotocedor.SocioTorcedorService;
import com.vas.desafioseniorsociotocedor.models.SocioTorcedor;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/socio-torcedor")
public class SocioTorcedorController {

	@Autowired
	private final SocioTorcedorService service;

	@PostMapping
	public ResponseEntity<SocioTorcedor> create(@RequestBody SocioTorcedor socioTorcedor) {
		SocioTorcedor createdSocioTorcedor = service.create(socioTorcedor);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(createdSocioTorcedor.getEmail()).toUri();
		return ResponseEntity.created(location).body(createdSocioTorcedor);
	}

	@GetMapping
	public ResponseEntity<List<SocioTorcedor>> getVigentes() {
		return ResponseEntity.ok(service.findAll());
	}

}
