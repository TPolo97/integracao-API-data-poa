package com.example.demo;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.controller.LinhaController;
import com.example.demo.model.Linha;
import com.example.demo.service.ItinerarioService;
import com.example.demo.service.LinhaService;

@WebMvcTest
class LinhaTests {
	
	@MockBean
	private LinhaService linhaService;
	
	@MockBean
	private ItinerarioService itinerarioService;
	
	@Autowired
	private LinhaController linhaController;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(linhaController).build();
	}

	@Test
	void deveRetornarLista() throws Exception {
		
		Mockito.when(linhaService.listaLinhas()).thenReturn(new ArrayList<Linha>());
		mockMvc.perform(get("/linha")).andExpect(status().isOk());
		
	}
	
	@Test
	void deveRetornarTodasComMesmoNome() throws Exception {
		Linha linha1 = new Linha(1L, "123", "TESTE");
		Linha linha2 = new Linha(2L, "456", "TESTE");
		Linha linha3 = new Linha(3L, "789", "TESTE");
		
		Mockito.when(linhaService.buscaPorNome("TESTE")).thenReturn(new ArrayList<Linha>(Arrays.asList(linha1, linha2, linha3)));
		mockMvc.perform(
				get("/linha/nome").
				param("nome", "TESTE")).
				andExpect(status().isOk()).
				andExpect(jsonPath("$", Matchers.hasSize(3))).
				andExpect(jsonPath("$[0].nome", Matchers.is("TESTE")));
		
	}

}
