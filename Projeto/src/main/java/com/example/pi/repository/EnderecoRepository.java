package com.example.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pi.model.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
