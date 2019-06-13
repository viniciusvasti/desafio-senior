package com.vas.desafioseniorcampanhas.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.vas.desafioseniorcampanhas.commands.CreateCampanhaCommand;
import com.vas.desafioseniorcampanhas.dtos.CampanhaDTO;
import com.vas.desafioseniorcampanhas.services.CampanhaService;

@WebMvcTest(CampanhaController.class)
public class CampanhaControllerTest extends BaseControllerTest {

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
						.value(createCampanhaCommand.getDataFimVigencia().toString()));

	}

}
