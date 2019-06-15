package com.vas.desafioseniorcampanhas.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vas.desafioseniorcampanhas.commands.CreateCampanhaCommand;
import com.vas.desafioseniorcampanhas.commands.UpdateCampanhaCommand;
import com.vas.desafioseniorcampanhas.dtos.CampanhaDTO;
import com.vas.desafioseniorcampanhas.exceptions.GenericBadRequestException;
import com.vas.desafioseniorcampanhas.models.Campanha;
import com.vas.desafioseniorcampanhas.repositories.CampanhaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CampanhaService {

	@Autowired
	private final CampanhaRepository campanhaRepository;

	public CampanhaDTO create(CreateCampanhaCommand command) {
		Campanha newCampanha = campanhaRepository.save(mapCreateCampanhaCommandToCampanha(command));
		return mapCampanhaToCampanhaDTO(newCampanha);
	}

	public List<CampanhaDTO> findAllVigentes() {
		return new ArrayList<>();
	}

	public CampanhaDTO update(UpdateCampanhaCommand command) {
		Campanha existingCampanha = campanhaRepository.findById(command.getId()).orElseThrow(
				() -> new GenericBadRequestException("Não há campanha com o id informado"));
		if (command.getDataFimVigencia() != null)
			existingCampanha.setDataFimVigencia(command.getDataFimVigencia());
		if (command.getNome() != null)
			existingCampanha.setNome(command.getNome());
		Campanha updatedCampanha = campanhaRepository.save(existingCampanha);
		return mapCampanhaToCampanhaDTO(updatedCampanha);
	}

	private static CampanhaDTO mapCampanhaToCampanhaDTO(Campanha campanha) {
		return new CampanhaDTO(campanha.getId(), campanha.getNome(),
				campanha.getIdTimeDoCoracao(), campanha.getDataFimVigencia());
	}

	private static Campanha mapCreateCampanhaCommandToCampanha(CreateCampanhaCommand command) {
		return new Campanha(null, command.getNome(), command.getIdTimeDoCoracao(),
				command.getDataFimVigencia());
	}

	public List<Campanha> findByVigencia(LocalDate vigencia) {
		return campanhaRepository.findByVigencia(vigencia);
	}

}
