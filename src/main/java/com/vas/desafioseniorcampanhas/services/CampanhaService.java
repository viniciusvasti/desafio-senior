package com.vas.desafioseniorcampanhas.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public CampanhaDTO create(CreateCampanhaCommand command) {
		fixVigenciasRecursively(command.getDataFimVigencia());
		Campanha newCampanha = campanhaRepository.save(mapCreateCampanhaCommandToCampanha(command));
		return mapCampanhaToCampanhaDTO(newCampanha);
	}

	public List<CampanhaDTO> findAllVigentes() {
		List<CampanhaDTO> campanhaDTOs = new ArrayList<>();
		List<Campanha> campanhaVigentes = campanhaRepository
				.findByDataFimVigenciaGreaterThanEqual(LocalDate.now());
		campanhaVigentes.forEach(campanha -> {
			campanhaDTOs.add(mapCampanhaToCampanhaDTO(campanha));
		});
		return campanhaDTOs;
	}

	public CampanhaDTO update(UpdateCampanhaCommand command) {
		Campanha existingCampanha = findById(command.getId());
		if (command.getDataFimVigencia() != null) {
			existingCampanha.setDataFimVigencia(command.getDataFimVigencia());
			fixVigenciasRecursively(command.getDataFimVigencia());
		}
		if (command.getNome() != null)
			existingCampanha.setNome(command.getNome());
		Campanha updatedCampanha = update(existingCampanha);
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
		return campanhaRepository.findByDataFimVigencia(vigencia);
	}

	public void deleteById(String idCampanha) {
		Campanha existingCampanha = findById(idCampanha);
		campanhaRepository.delete(existingCampanha);
	}

	public Campanha findById(String idCampanha) {
		return campanhaRepository.findById(idCampanha).orElseThrow(
				() -> new GenericBadRequestException("Não há campanha com o id informado"));
	}

	private void fixVigenciasRecursively(LocalDate vigencia) {
		List<Campanha> campanhasWithSameVigencia = findByVigencia(vigencia);
		if (!campanhasWithSameVigencia.isEmpty()) {
			for (Iterator<Campanha> iterator = campanhasWithSameVigencia.iterator(); iterator
					.hasNext();) {
				Campanha campanha = iterator.next();
				campanha.setDataFimVigencia(campanha.getDataFimVigencia().plusDays(1));
				fixVigenciasRecursively(campanha.getDataFimVigencia());
				campanhaRepository.save(campanha);
			}
		}
	}

	private Campanha update(Campanha campanha) {
		// TODO notify others systems about the changes
		return campanhaRepository.save(campanha);
	}

}
