package com.example.pi.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pi.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}