package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Linha;

@Repository
public interface LinhaRepository extends JpaRepository<Linha, Long>{

	public Optional<Linha> findByCodigo(String codigo);
	
	public Optional<List<Linha>> findByNome(String nome);
	
}
