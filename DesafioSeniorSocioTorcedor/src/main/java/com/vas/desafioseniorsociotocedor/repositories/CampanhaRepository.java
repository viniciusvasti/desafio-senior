package com.vas.desafioseniorsociotocedor.repositories;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vas.desafioseniorsociotocedor.models.Campanha;

@Repository
public interface CampanhaRepository extends MongoRepository<Campanha, String> {

	Set<Campanha> findByIdTimeDoCoracaoAndDataFimVigenciaGreaterThanEqual(long idTimeDoCoracao,
			LocalDate dataFimVigencia);

}
