package com.vas.desafioseniorsociotocedor.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vas.desafioseniorsociotocedor.DesafioSeniorSocioTorcedorApplication;
import com.vas.desafioseniorsociotocedor.consumers.CampanhaConsumer;
import com.vas.desafioseniorsociotocedor.models.SocioTorcedor;
import com.vas.desafioseniorsociotocedor.repositories.SocioTorcedorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
		DesafioSeniorSocioTorcedorApplication.class })
@EmbeddedKafka
@TestPropertySource(locations = "classpath:/application-test.properties")
public class SocioTorcedorControllerIntegrationTest {

	MockMvc mockMvc;
	@Autowired
	private SocioTorcedorController socioTorcedorController;
	@Autowired
	private SocioTorcedorRepository socioTorcedorRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private CampanhaConsumer campanhaConsumer;

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.socioTorcedorController).build();
	}

	@Test
	public void create_socioTorcedor_shouldReturnCreatedAndsocioTorcedor()
			throws JsonProcessingException, Exception {
		socioTorcedorRepository.deleteAll();
		SocioTorcedor socioTorcedor = new SocioTorcedor("email@email.com", "nome",
				LocalDate.now().minusYears(30), 1);

		mockMvc.perform(post("/socios-torcedores")
				.content(objectMapper.writeValueAsString(socioTorcedor))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nome").value(socioTorcedor.getNome()))
				.andExpect(jsonPath("$.email").value(socioTorcedor.getEmail()))
				.andExpect(jsonPath("$.idTimeDoCoracao")
						.value(socioTorcedor.getIdTimeDoCoracao()))
				.andExpect(jsonPath("$.dataNascimento[0]")
						.value(socioTorcedor.getDataNascimento().getYear()))
				.andExpect(jsonPath("$.dataNascimento[1]")
						.value(socioTorcedor.getDataNascimento().getMonthValue()))
				.andExpect(jsonPath("$.dataNascimento[2]")
						.value(socioTorcedor.getDataNascimento().getDayOfMonth()));

		assertTrue(socioTorcedorRepository.count() == 1);
	}

	@Test
	public void getAll_shouldReturnOkAndListOfSociosTorcedores() throws Exception {
		socioTorcedorRepository.deleteAll();
		List<SocioTorcedor> socioTorcedores = new ArrayList<>();
		socioTorcedores.add(
				new SocioTorcedor("jose@email.com", "jose", LocalDate.now().minusYears(30), 1));
		socioTorcedores.add(
				new SocioTorcedor("maria@email.com", "maria", LocalDate.now().minusYears(30), 1));
		socioTorcedores.add(
				new SocioTorcedor("joao@email.com", "joao", LocalDate.now().minusYears(30), 2));
		socioTorcedores.add(
				new SocioTorcedor("carlos@email.com", "carlos", LocalDate.now().minusYears(30), 1));
		socioTorcedores.add(
				new SocioTorcedor("astrogilda@email.com", "astrogilda",
						LocalDate.now().minusYears(30), 2));
		socioTorcedorRepository.saveAll(socioTorcedores);

		MvcResult result = mockMvc.perform(get("/socios-torcedores")).andExpect(status().isOk())
				.andReturn();
		List<SocioTorcedor> socioTorcedoresResponse = objectMapper
				.readValue(result.getResponse().getContentAsString(),
						new TypeReference<List<SocioTorcedor>>() {
						});

		assertEquals(5, socioTorcedoresResponse.size());
	}

}
