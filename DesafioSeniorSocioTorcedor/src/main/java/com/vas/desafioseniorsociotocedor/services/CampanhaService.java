package com.vas.desafioseniorsociotocedor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vas.desafioseniorsociotocedor.dtos.CampanhaDTO;
import com.vas.desafioseniorsociotocedor.models.Campanha;
import com.vas.desafioseniorsociotocedor.repositories.CampanhaRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CampanhaService {

	@Autowired
	private final CampanhaRepository repository;
	@Autowired
	private final SocioTorcedorService socioTorcedorService;

	public void create(CampanhaDTO campanhaDTO) {
		Campanha campanha = repository.save(mapCampanhaDTOToCampanha(campanhaDTO));
		socioTorcedorService.associateCampanhaByIdTimeDoCoracao(campanha);
	}

	public void update(CampanhaDTO campanhaDTO) {
		repository.save(mapCampanhaDTOToCampanha(campanhaDTO));
	}

	public void delete(CampanhaDTO campanhaDTO) {
		Campanha campanha = repository.findById(campanhaDTO.getId()).orElse(null);
		if (campanha != null) {
			repository.deleteById(campanha.getId());
		}
	}

	private static Campanha mapCampanhaDTOToCampanha(CampanhaDTO command) {
		return new Campanha(command.getId(), command.getNome(), command.getIdTimeDoCoracao(),
				command.getDataFimVigencia());
	}

}
