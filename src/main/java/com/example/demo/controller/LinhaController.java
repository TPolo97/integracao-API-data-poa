package com.example.demo.controller;

import java.util.List;

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

import com.example.demo.model.Linha;
import com.example.demo.service.LinhaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Linha")
@RequestMapping("/linha")
public class LinhaController {

	@Autowired
	private LinhaService linhaService;
	
	@ApiOperation(value = "Listar todas as linhas de ônibus")
	@GetMapping
	public ResponseEntity<List<Linha>> listaLinhas() throws Exception {
		return new ResponseEntity<List<Linha>>(linhaService.listaLinhas(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Filtar linhas de ônibus pelo nome")
	@GetMapping("/nome")
	public ResponseEntity<List<Linha>> buscaNome(@RequestParam String nome) throws Exception {
		return new ResponseEntity<List<Linha>>(linhaService.buscaPorNome(nome), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Buscar linhas de ônibus dentro do raio de distância")
	@GetMapping("/busca")
	public ResponseEntity<List<Linha>> buscaRaio(@RequestParam String raio, @RequestParam String lat, @RequestParam String lng) throws Exception {
		return new ResponseEntity<List<Linha>>(linhaService.buscaRaio(raio, lat, lng), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Cadastrar nova linha de ônibus")
	@PostMapping
	public ResponseEntity<Linha> cadastraLinha(@RequestBody Linha linha) throws Exception{
		return new ResponseEntity<Linha>(linhaService.cadastraLinha(linha), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Editar linha de ônibus")
	@PutMapping("/{id}")
	public ResponseEntity<Linha> editaLinha(@RequestBody Linha linha, @PathVariable Long id) throws Exception{
		return new ResponseEntity<Linha>(linhaService.editaLinha(id, linha), HttpStatus.OK);
	}
	
	
}
