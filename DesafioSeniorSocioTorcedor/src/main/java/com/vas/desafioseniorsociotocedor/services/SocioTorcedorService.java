package com.vas.desafioseniorsociotocedor.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vas.desafioseniorsociotocedor.exceptions.GenericBadRequestException;
import com.vas.desafioseniorsociotocedor.models.Campanha;
import com.vas.desafioseniorsociotocedor.models.SocioTorcedor;
import com.vas.desafioseniorsociotocedor.repositories.CampanhaRepository;
import com.vas.desafioseniorsociotocedor.repositories.SocioTorcedorRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SocioTorcedorService {

	@Autowired
	private SocioTorcedorRepository repository;
	@Autowired
	private CampanhaRepository campanhaRepository;

	public SocioTorcedor create(SocioTorcedor socioTorcedor) {
		validateCreate(socioTorcedor);
		Set<Campanha> campanhas = campanhaRepository
				.findByIdTimeDoCoracaoAndDataFimVigenciaGreaterThanEqual(
						socioTorcedor.getIdTimeDoCoracao(),
						LocalDate.now());
		socioTorcedor.setCampanhas(campanhas);
		return repository.save(socioTorcedor);
	}

	private void validateCreate(SocioTorcedor socioTorcedor) {
		Optional<SocioTorcedor> existingSocioTorcedor = repository
				.findById(socioTorcedor.getEmail());
		if (existingSocioTorcedor.isPresent()) {
			throw new GenericBadRequestException("O email informado já está cadastrado.");
		}
	}

	public List<SocioTorcedor> findAll() {
		return repository.findAll();
	}

	public void disassociateCampanhaByIdTimeDoCoracao(Campanha campanha) {
		List<SocioTorcedor> sociosTorcedores = repository
				.findByIdTimeDoCoracao(campanha.getIdTimeDoCoracao());
		sociosTorcedores.forEach(st -> {
			st.getCampanhas().remove(campanha);
			repository.save(st);
		});
	}

	public void associateCampanhaByIdTimeDoCoracao(Campanha campanha) {
		List<SocioTorcedor> sociosTorcedores = repository
				.findByIdTimeDoCoracao(campanha.getIdTimeDoCoracao());
		sociosTorcedores.forEach(st -> {
			st.getCampanhas().add(campanha);
			repository.save(st);
		});
	}

}
