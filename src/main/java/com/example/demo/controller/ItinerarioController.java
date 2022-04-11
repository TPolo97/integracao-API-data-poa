package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Itinerario;
import com.example.demo.service.ItinerarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Itinerario")
@RequestMapping("/itinerario")
public class ItinerarioController {
	
	@Autowired
	private ItinerarioService itinerarioService;
	
	@ApiOperation(value = "Listar itinerario por linha de ônibus")
	@GetMapping
	public ResponseEntity<Itinerario> listaItinerario(@RequestParam String idLinha) throws Exception {
		return new ResponseEntity<Itinerario>(itinerarioService.listaEtinerario(idLinha), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Cadastrar novo itinerario")
	@PostMapping
	public ResponseEntity<Itinerario> cadastraIninerario(@RequestBody Itinerario itinerario) throws IllegalArgumentException, IllegalAccessException{
		return new ResponseEntity<Itinerario>(itinerarioService.cadastraItinerario(itinerario), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Editar itinerario de ônibus")
	@PutMapping("/{id}")
	public ResponseEntity<Itinerario> editaLinha(@RequestBody Itinerario itinerario, @PathVariable Long id) throws Exception{
		return new ResponseEntity<Itinerario>(itinerarioService.editaItinerario(id, itinerario), HttpStatus.OK);
	}

}
