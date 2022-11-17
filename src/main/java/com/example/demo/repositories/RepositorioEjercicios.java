package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Ejercicio;

@Repository
public interface RepositorioEjercicios extends JpaRepository<Ejercicio, Long> {

	public List<Ejercicio> findByTipo(String tipo); 
}
