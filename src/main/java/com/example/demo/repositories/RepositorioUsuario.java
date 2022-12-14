package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Usuario;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Long>{

	public Usuario findByNombreUsuario (String nombreUsuario);
	
}
