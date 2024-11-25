package com.example.pi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pi.model.ImagemProduto;

public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, Long> {

    Optional<ImagemProduto> findByProdutoIdAndPrincipal(Long idProduto, boolean principal);
    List<ImagemProduto> findByProdutoId(Long idProduto);
}
