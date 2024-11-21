package com.example.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pi.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    Compra findByNumeroPedido(int numeroPedido);
}
