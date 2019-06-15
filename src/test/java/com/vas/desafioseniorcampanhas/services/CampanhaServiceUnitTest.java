package com.vas.desafioseniorcampanhas.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.vas.desafioseniorcampanhas.commands.CreateCampanhaCommand;
import com.vas.desafioseniorcampanhas.commands.UpdateCampanhaCommand;
import com.vas.desafioseniorcampanhas.dtos.CampanhaDTO;
import com.vas.desafioseniorcampanhas.exceptions.GenericBadRequestException;
import com.vas.desafioseniorcampanhas.models.Campanha;
import com.vas.desafioseniorcampanhas.repositories.CampanhaRepository;

@RunWith(MockitoJUnitRunner.class)
public class CampanhaServiceUnitTest {

	@Mock
	private CampanhaRepository campanhaRepository;
	
	@InjectMocks
	private CampanhaService campanhaService;
	
	@Test
	public void create_campanha_shouldCreateAndReturnNovaCampanha() {
		LocalDate vigencia = LocalDate.now().plusDays(30);
		CreateCampanhaCommand createCampanhaCommand = new CreateCampanhaCommand("Teste", 1,
				vigencia);
		CampanhaDTO campanhaDTO = new CampanhaDTO("24e235v4rwe", "Teste", 1, vigencia);
		Campanha campanha = new Campanha("24e235v4rwe", "Teste", 1, vigencia);

		when(campanhaRepository.save(any(Campanha.class))).thenReturn(campanha);

		CampanhaDTO campanhaDTOcreated = campanhaService.create(createCampanhaCommand);
		assertEquals(campanhaDTO.getNome(), campanhaDTOcreated.getNome());
		assertEquals(campanhaDTO.getIdTimeDoCoracao(), campanhaDTOcreated.getIdTimeDoCoracao());
		assertEquals(campanhaDTO.getDataFimVigencia(), campanhaDTOcreated.getDataFimVigencia());
	}

	@Test
	public void findById_Id_shouldReturnCampanha() {
		String idCampanha = "d4234as54das";
		Campanha campanha = new Campanha(idCampanha, "Teste", 1, LocalDate.now());

		when(campanhaRepository.findById(any(String.class)))
				.thenReturn(Optional.of(campanha));

		Campanha foundCampanha = campanhaService.findById(idCampanha);
		assertEquals(campanha.getId(), foundCampanha.getId());
		assertEquals(campanha.getNome(), foundCampanha.getNome());
		assertEquals(campanha.getIdTimeDoCoracao(), foundCampanha.getIdTimeDoCoracao());
		assertEquals(campanha.getDataFimVigencia(), foundCampanha.getDataFimVigencia());
	}

	@Test(expected = GenericBadRequestException.class)
	public void findById_nonExistingId_shouldThrowGenericBadRequestException() {
		String idCampanha = "d4234as54das";

		when(campanhaRepository.findById(any(String.class)))
				.thenReturn(Optional.empty());

		campanhaService.findById(idCampanha);
	}

	@Test
	public void update_campanha_shouldUpdateAndReturnUpdatedCampanha() {
		LocalDate vigencia = LocalDate.now().plusDays(30);
		UpdateCampanhaCommand createCampanhaCommand = new UpdateCampanhaCommand("24e235v4rwe",
				"Teste", vigencia);
		CampanhaDTO campanhaDTO = new CampanhaDTO("24e235v4rwe", "Teste", 1, vigencia);
		Campanha campanha = new Campanha("24e235v4rwe", "Teste", 1, vigencia);
		Campanha existingCampanha = new Campanha("24e235v4rwe", "Teste old", 1, vigencia);

		when(campanhaRepository.findById(any(String.class)))
				.thenReturn(Optional.of(existingCampanha));
		when(campanhaRepository.save(any(Campanha.class))).thenReturn(campanha);

		CampanhaDTO campanhaDTOupdated = campanhaService.update(createCampanhaCommand);
		assertEquals(campanhaDTO.getNome(), campanhaDTOupdated.getNome());
		assertEquals(campanhaDTO.getIdTimeDoCoracao(), campanhaDTOupdated.getIdTimeDoCoracao());
		assertEquals(campanhaDTO.getDataFimVigencia(), campanhaDTOupdated.getDataFimVigencia());
	}

	@Test
	public void delete_campanha_shouldDeleteCampanha() {
		String idCampanha = "d4234as54das";

		Campanha existingCampanha = new Campanha(idCampanha, "Teste old", 1, LocalDate.now());

		when(campanhaRepository.findById(any(String.class)))
				.thenReturn(Optional.of(existingCampanha));

		campanhaService.deleteById(idCampanha);
	}

	@Test
	public void findByVigencia_localDate_shouldReturnEmptyList() {
		LocalDate vigencia = LocalDate.now().plusDays(30);

		when(campanhaRepository.findByVigencia(any(LocalDate.class)))
				.thenReturn(Collections.emptyList());
		
		assertTrue(campanhaService.findByVigencia(vigencia).isEmpty());
	}
	
	@Test
	public void findByVigencia_localDate_shouldReturnListWith1Campanha() {
		LocalDate vigencia = LocalDate.now().plusDays(30);
		
		List<Campanha> campanhas = new ArrayList<>();
		campanhas.add(new Campanha("24e235v4rwe", "Teste", 1, vigencia));
		
		when(campanhaRepository.findByVigencia(any(LocalDate.class)))
		.thenReturn(campanhas);
		
		assertFalse(campanhaService.findByVigencia(vigencia).isEmpty());
	}

}
