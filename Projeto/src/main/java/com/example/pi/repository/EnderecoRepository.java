package com.example.pi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pi.model.Endereco;

import jakarta.transaction.Transactional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Endereco e SET e.principal = :principal WHERE e.cliente.id = :userId")
    void updatePrincipalStatus(@Param("userId") Long userId, @Param("principal") boolean principal);

    List<Endereco> findByClienteId(Long clienteId);
}
