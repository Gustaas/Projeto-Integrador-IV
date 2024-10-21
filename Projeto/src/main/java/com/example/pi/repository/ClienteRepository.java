package com.example.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pi.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByEmail(String email);
    Cliente findByCpf(String cpf);
}
