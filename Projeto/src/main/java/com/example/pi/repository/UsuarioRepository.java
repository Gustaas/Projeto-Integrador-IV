package com.example.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}