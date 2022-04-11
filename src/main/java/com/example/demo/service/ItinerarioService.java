package com.example.demo.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.model.Cordenada;
import com.example.demo.model.Itinerario;
import com.example.demo.model.Linha;
import com.example.demo.repository.ItinerarioRepository;

@Service
public class ItinerarioService {

	@Autowired
	private ItinerarioRepository itinerarioRepository;

	@Autowired
	private LinhaService linhaService;

	@Autowired
	private CordenadaService cordenadaService;

	@Autowired
	private RestTemplate restTemplate;

	public Itinerario listaEtinerario(String idLinha) throws Exception {
		Linha linha = linhaService.buscaPorId(Long.parseLong(idLinha));
		if (!itinerarioRepository.findByLinha(linha).isPresent()) {
			UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http")
					.host("www.poatransporte.com.br").path("php/facades/process.php").queryParam("a", "il")
					.queryParam("p", idLinha).build();

			ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriComponents.toString(), String.class);
			JSONObject jsonObject = new JSONObject(responseEntity.getBody());
			Iterator<String> keys = jsonObject.keys();
			Itinerario itinerario = new Itinerario();
			itinerario.setLinha(linha);
			List<Cordenada> listCordenadas = new ArrayList<Cordenada>();
			while (keys.hasNext()) {
				String key = keys.next();
				if (!key.equals("idlinha") && !key.equals("codigo") && !key.equals("nome")) {
					Cordenada cordenada = new Cordenada();
					cordenada.setNumero(Long.parseLong(key));
					cordenada.setLat(jsonObject.getJSONObject(key).getString("lat"));
					cordenada.setLng(jsonObject.getJSONObject(key).getString("lng"));
					cordenada = cordenadaService.salvaCordenada(cordenada);
					listCordenadas.add(cordenada);
				}
			}
			itinerario.setCordenadas(listCordenadas);
			itinerarioRepository.save(itinerario);
		}

		Itinerario itinerario = itinerarioRepository.findByLinha(linha).get();
		itinerario.getCordenadas().sort(Comparator.comparing(Cordenada::getNumero));
		return itinerario;
	}

	public Itinerario cadastraItinerario(Itinerario novoItinerario) throws IllegalArgumentException, IllegalAccessException {
		Itinerario itinerario = itinerarioRepository.findByLinha(novoItinerario.getLinha()).get();
		if (itinerario == null) {
			List<Cordenada> listCordenadas = new ArrayList<Cordenada>();
			for (Cordenada cordenada : novoItinerario.getCordenadas()) {
				listCordenadas.add(cordenadaService.salvaCordenada(cordenada));
			}
			novoItinerario.setCordenadas(listCordenadas);
			return itinerarioRepository.save(novoItinerario);
		} else {
			return editaItinerario(itinerario.getId(), novoItinerario);
		}

	}

	public Itinerario editaItinerario(Long id, Itinerario novoItinerario) throws IllegalArgumentException, IllegalAccessException {
		Itinerario itinerario = itinerarioRepository.findById(id).get();
		for(Field campo : novoItinerario.getClass().getDeclaredFields()) {
			campo.setAccessible(true);
			if (campo.getName().equals("cordenadas")) {
				List<Cordenada> cordenadas = (List<Cordenada>) campo.get(novoItinerario);
				cordenadas.forEach(c -> {
					try {
						Cordenada cordenada = itinerario.getCordenadas().stream().filter(cord -> cord.getNumero().equals(c.getNumero())).collect(Collectors.toList()).get(0);
						cordenadaService.editaCordenada(cordenada.getId(), c);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				});
			} else if (campo.get(novoItinerario) != null && !campo.get(itinerario).equals(campo.get(novoItinerario))) {
				campo.set(itinerario, campo.get(novoItinerario));
			}
		}
		Itinerario i = itinerarioRepository.save(itinerario);
		i.getCordenadas().sort(Comparator.comparing(Cordenada::getNumero));
		return i;
	}

}
