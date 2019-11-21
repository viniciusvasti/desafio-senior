package com.vas.desafioseniorsociotocedor.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.vas.desafioseniorsociotocedor.DesafioSeniorSocioTorcedorApplication;
import com.vas.desafioseniorsociotocedor.consumers.CampanhaConsumer;
import com.vas.desafioseniorsociotocedor.models.Campanha;
import com.vas.desafioseniorsociotocedor.models.SocioTorcedor;
import com.vas.desafioseniorsociotocedor.repositories.CampanhaRepository;
import com.vas.desafioseniorsociotocedor.repositories.SocioTorcedorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
		DesafioSeniorSocioTorcedorApplication.class })
@EmbeddedKafka
@TestPropertySource(locations = "classpath:/application-test.properties")
public class SocioTorcedorServiceIntegrationTest {

	MockMvc mockMvc;
	@Autowired
	private SocioTorcedorService socioTorcedorService;
	@Autowired
	private SocioTorcedorRepository socioTorcedorRepository;
	@Autowired
	private CampanhaRepository campanhaRepository;
	@MockBean
	private CampanhaConsumer campanhaConsumer;

	@Test
	public void create_socioTorcedorWithNoCampanhasForHim_shouldReturnSocioTorncedorWithEmptyListOfCampanhas() {
		socioTorcedorRepository.deleteAll();
		campanhaRepository.deleteAll();
		campanhaRepository.save(new Campanha("1", "teste1", 2, LocalDate.now()));
		SocioTorcedor socioTorcedor = new SocioTorcedor("email@email.com", "nome",
				LocalDate.now().minusYears(30), 1);
		SocioTorcedor newSocioTorcedor = socioTorcedorService.create(socioTorcedor);
		assertTrue(newSocioTorcedor.getCampanhas().isEmpty());
		assertEquals(socioTorcedor.getEmail(), newSocioTorcedor.getEmail());
		assertEquals(socioTorcedor.getNome(), newSocioTorcedor.getNome());
		assertEquals(socioTorcedor.getIdTimeDoCoracao(), newSocioTorcedor.getIdTimeDoCoracao());
		assertEquals(socioTorcedor.getDataNascimento(), newSocioTorcedor.getDataNascimento());
	}

	@Test
	public void create_socioTorcedorWith2CampanhasForHim_shouldReturnSocioTorncedorWithListOfCampanhasWithSize2() {
		socioTorcedorRepository.deleteAll();
		campanhaRepository.deleteAll();
		campanhaRepository.save(new Campanha("1", "teste1", 1, LocalDate.now()));
		campanhaRepository.save(new Campanha("2", "teste2", 1, LocalDate.now()));
		campanhaRepository.save(new Campanha("3", "teste3", 2, LocalDate.now()));
		SocioTorcedor socioTorcedor = new SocioTorcedor("email@email.com", "nome",
				LocalDate.now().minusYears(30), 1);
		SocioTorcedor newSocioTorcedor = socioTorcedorService.create(socioTorcedor);
		assertEquals(2, newSocioTorcedor.getCampanhas().size());
		assertEquals(socioTorcedor.getEmail(), newSocioTorcedor.getEmail());
		assertEquals(socioTorcedor.getNome(), newSocioTorcedor.getNome());
		assertEquals(socioTorcedor.getIdTimeDoCoracao(), newSocioTorcedor.getIdTimeDoCoracao());
		assertEquals(socioTorcedor.getDataNascimento(), newSocioTorcedor.getDataNascimento());
	}

	@Test
	public void associateCampanhaByIdTimeDoCoracao_shouldAssociateCampanhaWithSociosTorcedoresByIdTimeDoCoracao() {
		campanhaRepository.deleteAll();
		socioTorcedorRepository.deleteAll();
		SocioTorcedor socioTorcedor = new SocioTorcedor("email@email.com", "nome",
				LocalDate.now().minusYears(30), 1);
		socioTorcedor = socioTorcedorService.create(socioTorcedor);
		assertEquals(0, socioTorcedor.getCampanhas().size());
		Campanha campanha = campanhaRepository
				.save(new Campanha("1", "teste1", 1, LocalDate.now()));
		socioTorcedorService.associateCampanhaByIdTimeDoCoracao(campanha);
		socioTorcedor = socioTorcedorRepository.findById(socioTorcedor.getEmail()).get();
		assertEquals(1, socioTorcedor.getCampanhas().size());
	}

}
