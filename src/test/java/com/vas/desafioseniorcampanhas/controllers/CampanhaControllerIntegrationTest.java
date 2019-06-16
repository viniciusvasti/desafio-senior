package com.vas.desafioseniorcampanhas.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vas.desafioseniorcampanhas.DesafioSeniorCampanhasApplication;
import com.vas.desafioseniorcampanhas.commands.CreateCampanhaCommand;
import com.vas.desafioseniorcampanhas.models.Campanha;
import com.vas.desafioseniorcampanhas.repositories.CampanhaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
		DesafioSeniorCampanhasApplication.class })
@TestPropertySource(locations = "classpath:/application-test.properties")
public class CampanhaControllerIntegrationTest {

	MockMvc mockMvc;
	@Autowired
	private CampanhaController campanhaController;
	@Autowired
	private CampanhaRepository campanhaRepository;
	@Autowired
	protected ObjectMapper objectMapper;

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.campanhaController).build();
	}

	@Test
	public void post_campanhaWithNoConflictOnVigencia_shouldReturnCreatedAndCampanha()
			throws JsonProcessingException, Exception {
		campanhaRepository.deleteAll();
		CreateCampanhaCommand createCampanhaCommand = new CreateCampanhaCommand("Teste", 1,
				LocalDate.now().plusDays(30));

		mockMvc.perform(post("/campanhas")
				.content(objectMapper.writeValueAsString(createCampanhaCommand))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nome").value(createCampanhaCommand.getNome()))
				.andExpect(jsonPath("$.idTimeDoCoracao")
						.value(createCampanhaCommand.getIdTimeDoCoracao()))
				.andExpect(jsonPath("$.dataFimVigencia")
						.value(createCampanhaCommand.getDataFimVigencia().toString()))
				.andExpect(jsonPath("$.id").exists());

		assertTrue(campanhaRepository.count() == 1);
	}

	@Test
	public void post_campanhaWith1ConflictOnVigencia_shouldReturnCreatedAndCampanha()
			throws JsonProcessingException, Exception {
		campanhaRepository.deleteAll();
		LocalDate vigenciaPlus30 = LocalDate.now().plusDays(30);
		Campanha campanha1 = new Campanha("24e235v4rwe", "Teste", 1, vigenciaPlus30);
		campanha1 = campanhaRepository.save(campanha1);

		CreateCampanhaCommand createCampanhaCommand = new CreateCampanhaCommand("Teste2", 1,
				vigenciaPlus30);

		mockMvc.perform(post("/campanhas")
				.content(objectMapper.writeValueAsString(createCampanhaCommand))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nome").value(createCampanhaCommand.getNome()))
				.andExpect(jsonPath("$.idTimeDoCoracao")
						.value(createCampanhaCommand.getIdTimeDoCoracao()))
				.andExpect(jsonPath("$.dataFimVigencia")
						.value(createCampanhaCommand.getDataFimVigencia().toString()))
				.andExpect(jsonPath("$.id").exists());

		assertEquals(campanhaRepository.findById(campanha1.getId()).get().getDataFimVigencia(),
				vigenciaPlus30.plusDays(1));
	}

	@Test
	public void post_campanhaWith2ConflictOnVigencia_shouldFixVigenciasConflicsAndReturnCreatedAndCampanha()
			throws JsonProcessingException, Exception {
		campanhaRepository.deleteAll();
		LocalDate vigenciaPlus30 = LocalDate.now().plusDays(30);
		Campanha campanha1 = new Campanha(null, "Teste", 1, vigenciaPlus30);
		campanha1 = campanhaRepository.save(campanha1);
		Campanha campanha2 = new Campanha(null, "Teste2", 1, vigenciaPlus30.plusDays(1));
		campanha2 = campanhaRepository.save(campanha2);

		CreateCampanhaCommand createCampanhaCommand = new CreateCampanhaCommand("Teste3", 1,
				vigenciaPlus30);

		mockMvc.perform(post("/campanhas")
				.content(objectMapper.writeValueAsString(createCampanhaCommand))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nome").value(createCampanhaCommand.getNome()))
				.andExpect(jsonPath("$.idTimeDoCoracao")
						.value(createCampanhaCommand.getIdTimeDoCoracao()))
				.andExpect(jsonPath("$.dataFimVigencia")
						.value(createCampanhaCommand.getDataFimVigencia().toString()))
				.andExpect(jsonPath("$.id").exists());

		assertEquals(campanhaRepository.findById(campanha1.getId()).get().getDataFimVigencia(),
				vigenciaPlus30.plusDays(1));
		assertEquals(campanhaRepository.findById(campanha2.getId()).get().getDataFimVigencia(),
				vigenciaPlus30.plusDays(2));
	}

}