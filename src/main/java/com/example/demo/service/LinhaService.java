package com.example.demo.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.model.Cordenada;
import com.example.demo.model.Itinerario;
import com.example.demo.model.Linha;
import com.example.demo.repository.LinhaRepository;

@Service
public class LinhaService {

	@Autowired
	private LinhaRepository linhaRepository;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ItinerarioService itinerarioService;

	public List<Linha> listaLinhas() throws Exception {
		UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host("www.poatransporte.com.br")
				.path("php/facades/process.php").queryParam("a", "nc").queryParam("p", "%").queryParam("t", "o")
				.build();

		ResponseEntity<Linha[]> responseEntity = restTemplate.getForEntity(uriComponents.toString(), Linha[].class);
		for (Linha linha : responseEntity.getBody()) {
			if (buscaPorCodigo(linha.getCodigo()) == null)
				linhaRepository.save(linha);
		}
		return linhaRepository.findAll();
	}

	public List<Linha> buscaPorNome(String nome) throws Exception {
		return linhaRepository.findByNome(nome).orElseThrow(() -> new Exception("Linha não encontrada"));
	}

	public Linha buscaPorId(Long id) throws Exception {
		return linhaRepository.findById(id).orElseThrow(() -> new Exception("Linha não encontrada"));
	}

	public Linha buscaPorCodigo(String codigo) throws Exception {
		return linhaRepository.findByCodigo(codigo).orElseThrow(() -> new Exception("Linha não encontrada"));
	}

	public Linha cadastraLinha(Linha novaLinha) throws Exception {
		Linha linha = buscaPorCodigo(novaLinha.getCodigo());
		if (linha == null) {
			return linhaRepository.save(novaLinha);
		} else {
			return editaLinha(linha.getId(), novaLinha);
		}
	}

	public List<Linha> buscaRaio(String raio, String lat, String lng) throws Exception {
		
		Double latD = Math.toRadians(Double.parseDouble(lat));
		Double lngD = Double.parseDouble(lng);
		Double raioD = Double.parseDouble(raio);
		
		List<Linha> linhas = linhaRepository.findAll();
		List<Linha> linhasRaio = new ArrayList<>();
		for (Linha linha : linhas) {
			Itinerario itinerario = itinerarioService.listaEtinerario(linha.getId().toString());
			for (Cordenada cordenada : itinerario.getCordenadas()) {
				Double Xpoint = Math.toRadians(Double.parseDouble(cordenada.getLat()));
				Double Ypoint = Double.parseDouble(cordenada.getLng());
				
				Double lngDif = Math.toRadians(lngD - Ypoint);
				Double distancia = Math.acos(Math.cos(latD) * Math.cos(Xpoint) * Math.cos(lngDif) + Math.sin(latD) * Math.sin(Xpoint))	* 6371;

				if (distancia <= raioD) {
					linhasRaio.add(linha);
					break;
				}
			}
		}
		
		return linhasRaio;
	}

	public Linha editaLinha(Long id, Linha novaLinha) throws Exception {
		Linha linha = buscaPorId(id);
		for (Field campo : linha.getClass().getDeclaredFields()) {
			campo.setAccessible(true);
			if (campo.get(novaLinha) != null && !campo.get(linha).equals(campo.get(novaLinha))) {
				campo.set(linha, campo.get(novaLinha));
			}
		}
		return linhaRepository.save(linha);
	}

}
