package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Itinerario;
import com.example.demo.model.Linha;

@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long>{

	public Optional<Itinerario> findByLinha(Linha linha);
	
}
