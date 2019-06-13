
package com.vas.desafioseniorcampanhas.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.vas.desafioseniorcampanhas.models.Campanha;

@RunWith(SpringRunner.class)
@DataMongoTest
@TestPropertySource(locations = "classpath:/application-test.properties")
public class CampanhaRepositoryTest {

	@Autowired
	private CampanhaRepository campanhaRepository;

	@Test
	public void save_campanha_shouldReturnNewCampanhaWithId() {
		Campanha campanha = new Campanha();
		campanha.setNome("Teste");
		campanha.setIdTimeDoCoracao(1);
		campanha.setDataFimVigencia(LocalDate.now().plusDays(30));

		Campanha persistedCampanha = campanhaRepository.save(campanha);
		assertEquals(campanha.getNome(), persistedCampanha.getNome());
		assertTrue(campanha.getIdTimeDoCoracao() == persistedCampanha.getIdTimeDoCoracao());
		assertEquals(campanha.getDataFimVigencia(), persistedCampanha.getDataFimVigencia());
		assertNotNull(persistedCampanha.getId());
		assertFalse(persistedCampanha.getId().isEmpty());
	}

}
