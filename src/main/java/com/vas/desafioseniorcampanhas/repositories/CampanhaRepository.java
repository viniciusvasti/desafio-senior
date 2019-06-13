package com.vas.desafioseniorcampanhas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vas.desafioseniorcampanhas.models.Campanha;

@Repository
public interface CampanhaRepository extends CrudRepository<Campanha, String> {

}
