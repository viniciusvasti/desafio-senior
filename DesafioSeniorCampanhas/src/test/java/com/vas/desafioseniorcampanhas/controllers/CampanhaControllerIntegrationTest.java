package com.vas.desafioseniorcampanhas.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import com.vas.desafioseniorcampanhas.DesafioSeniorCampanhasApplication;
import com.vas.desafioseniorcampanhas.commands.CreateCampanhaCommand;
import com.vas.desafioseniorcampanhas.commands.UpdateCampanhaCommand;
import com.vas.desafioseniorcampanhas.dtos.CampanhaDTO;
import com.vas.desafioseniorcampanhas.events.CampanhaEventListener;
import com.vas.desafioseniorcampanhas.models.Campanha;
import com.vas.desafioseniorcampanhas.repositories.CampanhaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
		DesafioSeniorCampanhasApplication.class })
@EmbeddedKafka
@TestPropertySource(locations = "classpath:/application-test.properties")
public class CampanhaControllerIntegrationTest {

	MockMvc mockMvc;
	@Autowired
	private CampanhaController campanhaController;
	@Autowired
	private CampanhaRepository campanhaRepository;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CampanhaEventListener campanhaEventListener;

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
				.andExpect(jsonPath("$.dataFimVigencia[0]")
						.value(createCampanhaCommand.getDataFimVigencia().getYear()))
				.andExpect(jsonPath("$.dataFimVigencia[1]")
						.value(createCampanhaCommand.getDataFimVigencia().getMonthValue()))
				.andExpect(jsonPath("$.dataFimVigencia[2]")
						.value(createCampanhaCommand.getDataFimVigencia().getDayOfMonth()))
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
				.andExpect(jsonPath("$.dataFimVigencia[0]")
						.value(createCampanhaCommand.getDataFimVigencia().getYear()))
				.andExpect(jsonPath("$.dataFimVigencia[1]")
						.value(createCampanhaCommand.getDataFimVigencia().getMonthValue()))
				.andExpect(jsonPath("$.dataFimVigencia[2]")
						.value(createCampanhaCommand.getDataFimVigencia().getDayOfMonth()))
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
				.andExpect(jsonPath("$.dataFimVigencia[0]")
						.value(createCampanhaCommand.getDataFimVigencia().getYear()))
				.andExpect(jsonPath("$.dataFimVigencia[1]")
						.value(createCampanhaCommand.getDataFimVigencia().getMonthValue()))
				.andExpect(jsonPath("$.dataFimVigencia[2]")
						.value(createCampanhaCommand.getDataFimVigencia().getDayOfMonth()))
				.andExpect(jsonPath("$.id").exists());

		assertEquals(campanhaRepository.findById(campanha1.getId()).get().getDataFimVigencia(),
				vigenciaPlus30.plusDays(1));
		assertEquals(campanhaRepository.findById(campanha2.getId()).get().getDataFimVigencia(),
				vigenciaPlus30.plusDays(2));
	}

	@Test
	public void getVigentes_shouldReturnOkAndOnlyCampanhasVigentes() throws Exception {
		campanhaRepository.deleteAll();
		List<Campanha> campanhas = new ArrayList<>();
		campanhas.add(new Campanha(null, "Teste1", 1, LocalDate.now().plusDays(30)));
		campanhas.add(new Campanha(null, "Teste2", 2, LocalDate.now().plusDays(20)));
		campanhas.add(new Campanha(null, "Teste3", 1, LocalDate.now().minusDays(1)));
		campanhas.add(new Campanha(null, "Teste4", 3, LocalDate.now()));
		campanhas.add(new Campanha(null, "Teste5", 3, LocalDate.now().plusDays(1)));
		campanhaRepository.saveAll(campanhas);

		MvcResult result = mockMvc.perform(get("/campanhas")).andExpect(status().isOk())
				.andReturn();
		List<CampanhaDTO> campanhasResponse = objectMapper
				.readValue(result.getResponse().getContentAsString(),
						new TypeReference<List<CampanhaDTO>>() {
						});

		assertEquals(4, campanhasResponse.size());
		campanhasResponse.forEach(campanha -> {
			assertTrue(campanha.getDataFimVigencia().compareTo(LocalDate.now()) >= 0);
		});
	}

	@Test
	public void update_campanha_shouldDeleteById() throws Exception {
		campanhaRepository.deleteAll();
		LocalDate vigenciaPlus = LocalDate.now();
		Campanha campanha = new Campanha(null, "Teste", 1, vigenciaPlus);
		campanha = campanhaRepository.save(campanha);
		UpdateCampanhaCommand updateCampanhaCommand = new UpdateCampanhaCommand("24e235v4rwe",
				"Teste2", null);

		MvcResult result = mockMvc.perform(patch("/campanhas/" + campanha.getId())
				.content(objectMapper.writeValueAsString(updateCampanhaCommand))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		CampanhaDTO updatedCampanha = objectMapper
				.readValue(result.getResponse().getContentAsString(), CampanhaDTO.class);
		assertEquals(updateCampanhaCommand.getNome(), updatedCampanha.getNome());
	}

	@Test
	public void delete_idCampanha_shouldDeleteById() throws Exception {
		campanhaRepository.deleteAll();
		LocalDate vigenciaPlus = LocalDate.now();
		Campanha campanha = new Campanha(null, "Teste", 1, vigenciaPlus);
		campanha = campanhaRepository.save(campanha);

		mockMvc.perform(delete("/campanhas/" + campanha.getId()))
				.andExpect(status().isOk());
		assertFalse(campanhaRepository.findById(campanha.getId()).isPresent());
	}

}
