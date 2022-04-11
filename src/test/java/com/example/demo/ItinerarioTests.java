package com.example.demo;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.controller.ItinerarioController;
import com.example.demo.model.Itinerario;
import com.example.demo.service.ItinerarioService;

@WebMvcTest
class ItinerarioTests {
	
	@MockBean
	private ItinerarioService itinerarioService;
	
	@Autowired
	private ItinerarioController itinerarioController;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(itinerarioController).build();
	}

	@Test
	void deveRetornarItinerario() throws Exception {
		
		Mockito.when(itinerarioService.listaEtinerario("TESTE")).thenReturn(new Itinerario());
		mockMvc.perform(get("/itinerario")).andExpect(status().isOk());
		
	}

}
