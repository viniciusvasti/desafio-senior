package com.vas.desafioseniorcampanhas.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vas.desafioseniorcampanhas.commands.CreateCampanhaCommand;
import com.vas.desafioseniorcampanhas.commands.UpdateCampanhaCommand;
import com.vas.desafioseniorcampanhas.dtos.CampanhaDTO;
import com.vas.desafioseniorcampanhas.services.CampanhaService;

@RunWith(SpringRunner.class)
@WebMvcTest(CampanhaController.class)
public class CampanhaControllerUnitTest {

	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	private CampanhaService campanhaService;

	@Test
	public void post_campanha_shouldReturnCreatedAndCampanhaJson() throws Exception {

		CreateCampanhaCommand createCampanhaCommand = new CreateCampanhaCommand("Teste", 1,
				LocalDate.now().plusDays(30));
		CampanhaDTO campanhaDTO = new CampanhaDTO("24e235v4rwe", "Teste", 1,
				LocalDate.now().plusDays(30));

		when(campanhaService.create(any(CreateCampanhaCommand.class)))
				.thenReturn(campanhaDTO);

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

	}

	@Test
	public void update_campanha_shouldReturnOkAndCampanhaJson()
			throws JsonProcessingException, Exception {
		UpdateCampanhaCommand updateCampanhaCommand = new UpdateCampanhaCommand("24e235v4rwe",
				"Teste", LocalDate.now().plusDays(30));
		CampanhaDTO campanhaDTO = new CampanhaDTO("24e235v4rwe", "Teste", 1,
				LocalDate.now().plusDays(30));

		when(campanhaService.update(any(UpdateCampanhaCommand.class))).thenReturn(campanhaDTO);

		mockMvc.perform(put("/campanhas/" + updateCampanhaCommand.getId())
				.content(objectMapper.writeValueAsString(updateCampanhaCommand))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value(updateCampanhaCommand.getNome()))
				.andExpect(jsonPath("$.dataFimVigencia")
						.value(updateCampanhaCommand.getDataFimVigencia().toString()))
				.andExpect(jsonPath("$.id").value(campanhaDTO.getId()));
	}

	@Test
	public void getVigentes_campanhas_shouldReturnOkAndCampanhasJsonArray() throws Exception {
		List<CampanhaDTO> campanhas = new ArrayList<>();
		campanhas.add(new CampanhaDTO("24e235v4rwe", "Teste1", 1, LocalDate.now().plusDays(30)));
		campanhas.add(new CampanhaDTO("fsvrwrwe", "Teste2", 2, LocalDate.now().plusDays(20)));
		campanhas.add(new CampanhaDTO("wb534tb4", "Teste3", 1, LocalDate.now().plusDays(35)));
		campanhas.add(new CampanhaDTO("vvrwrv", "Teste4", 3, LocalDate.now().plusDays(22)));

		when(campanhaService.findAllVigentes()).thenReturn(campanhas);

		MvcResult result = mockMvc.perform(get("/campanhas")).andExpect(status().isOk())
				.andReturn();
		List<CampanhaDTO> campanhasResponse = objectMapper
				.readValue(result.getResponse().getContentAsString(),
						new TypeReference<List<CampanhaDTO>>() {
						});
		assertEquals(campanhas.size(), campanhasResponse.size());
		assertEquals(campanhas.get(0).getNome(), campanhasResponse.get(0).getNome());
		assertEquals(campanhas.get(1).getNome(), campanhasResponse.get(1).getNome());
		assertEquals(campanhas.get(2).getNome(), campanhasResponse.get(2).getNome());
		assertEquals(campanhas.get(3).getNome(), campanhasResponse.get(3).getNome());
	}

	@Test
	public void delete_campanhas_shouldReturnOk() throws Exception {
		String idCampanha = "rwerv7ewr76";

		mockMvc.perform(delete("/campanhas/" + idCampanha)).andExpect(status().isOk());
	}

}
