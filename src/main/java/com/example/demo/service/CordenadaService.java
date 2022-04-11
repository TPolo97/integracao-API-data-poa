package com.example.demo.service;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cordenada;
import com.example.demo.repository.CordenadaRepository;

@Service
public class CordenadaService {

	@Autowired
	private CordenadaRepository cordenadaRepository;

	public Cordenada salvaCordenada(Cordenada cordenada) throws IllegalArgumentException, IllegalAccessException {
		if (cordenada.getId() != null) {
			return editaCordenada(cordenada.getId(), cordenada);
		} else {
			return cordenadaRepository.save(cordenada);
		}
	}

	public Cordenada editaCordenada(Long id, Cordenada novaCordenada)
			throws IllegalArgumentException, IllegalAccessException {
		Cordenada cordenada = cordenadaRepository.findById(id).get();
		for (Field campo : novaCordenada.getClass().getDeclaredFields()) {
			campo.setAccessible(true);
			if (campo.get(novaCordenada) != null && !campo.get(cordenada).equals(campo.get(novaCordenada))) {
				campo.set(cordenada, campo.get(novaCordenada));
			}
		}
		return cordenadaRepository.save(cordenada);

	}

}
