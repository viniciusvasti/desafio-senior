package com.vas.desafioseniorsociotocedor.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vas.desafioseniorsociotocedor.models.SocioTorcedor;

@Repository
public interface SocioTorcedorRepository extends MongoRepository<SocioTorcedor, String> {
}
