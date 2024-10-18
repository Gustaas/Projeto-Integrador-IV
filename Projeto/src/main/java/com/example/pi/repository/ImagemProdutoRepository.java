package com.example.pi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pi.model.ImagemProduto;

public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, Long> {

    List<ImagemProduto> findByProdutoId(Long idProduto);
}
