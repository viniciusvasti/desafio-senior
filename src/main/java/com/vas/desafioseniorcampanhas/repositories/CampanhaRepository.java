package com.vas.desafioseniorcampanhas.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vas.desafioseniorcampanhas.models.Campanha;

@Repository
public interface CampanhaRepository extends MongoRepository<Campanha, String> {

	List<Campanha> findByDataFimVigencia(LocalDate dataFimVigencia);

	List<Campanha> findByDataFimVigenciaGreaterThanEqual(LocalDate dataFimVigencia);

}
