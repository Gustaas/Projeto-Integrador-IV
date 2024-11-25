package com.example.pi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.pi.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query("SELECT c FROM Compra c JOIN FETCH c.itens WHERE c.cliente.id = :idCliente")
    List<Compra> findByClienteIdWithItens(Long idCliente);

    Compra findByNumeroPedido(int numeroPedido);
}
