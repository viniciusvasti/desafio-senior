package com.vas.desafioseniorsociotocedor.services;

import static org.junit.Assert.assertEquals;

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
import com.vas.desafioseniorsociotocedor.broker.CampanhaConsumer;
import com.vas.desafioseniorsociotocedor.dtos.CampanhaDTO;
import com.vas.desafioseniorsociotocedor.models.SocioTorcedor;
import com.vas.desafioseniorsociotocedor.repositories.CampanhaRepository;
import com.vas.desafioseniorsociotocedor.repositories.SocioTorcedorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
		DesafioSeniorSocioTorcedorApplication.class })
@EmbeddedKafka
@TestPropertySource(locations = "classpath:/application-test.properties")
public class CampanhaServiceIntegrationTest {

	MockMvc mockMvc;
	@Autowired
	private CampanhaService campanhaService;
	@Autowired
	private SocioTorcedorService socioTorcedorService;
	@Autowired
	private SocioTorcedorRepository socioTorcedorRepository;
	@Autowired
	private CampanhaRepository campanhaRepository;
	@MockBean
	private CampanhaConsumer campanhaConsumer;

	@Test
	public void create_campanha_ShouldAssociateCamapanhaWithSociosTorcedoresByIdTimeDoCoracao() {
		campanhaRepository.deleteAll();
		socioTorcedorRepository.deleteAll();
		SocioTorcedor socioTorcedor = socioTorcedorRepository.save(
				new SocioTorcedor("email@email.com", "nome", LocalDate.now().minusYears(30), 1));
		assertEquals(0, socioTorcedor.getCampanhas().size());
		campanhaService.create(new CampanhaDTO("1", "teste1", 1, LocalDate.now()));
		socioTorcedor = socioTorcedorRepository.findById(socioTorcedor.getEmail()).get();
		assertEquals(1, socioTorcedor.getCampanhas().size());
	}


	@Test
	public void delete_Campanha_ShouldRemoveCamapanhaWithIdTimeDoCoracaoFromListOfCampanhasOfSociosTorcedores() {
		socioTorcedorRepository.deleteAll();
		campanhaRepository.deleteAll();
		CampanhaDTO campanhaDTO = new CampanhaDTO("1", "teste1", 1, LocalDate.now());
		campanhaService.create(campanhaDTO);
		campanhaService.create(new CampanhaDTO("2", "teste2", 1, LocalDate.now()));
		SocioTorcedor socioTorcedor = new SocioTorcedor("email@email.com", "nome",
				LocalDate.now().minusYears(30), 1);
		SocioTorcedor socioTorcedor2 = new SocioTorcedor("email2@email.com", "nome2",
				LocalDate.now().minusYears(30), 1);
		socioTorcedor = socioTorcedorService.create(socioTorcedor);
		socioTorcedor2 = socioTorcedorService.create(socioTorcedor2);

		campanhaService.delete(campanhaDTO);
		socioTorcedor = socioTorcedorRepository.findById(socioTorcedor.getEmail()).get();
		socioTorcedor2 = socioTorcedorRepository.findById(socioTorcedor2.getEmail()).get();
		assertEquals(1, socioTorcedor.getCampanhas().size());
		assertEquals(1, socioTorcedor2.getCampanhas().size());
	}

}
