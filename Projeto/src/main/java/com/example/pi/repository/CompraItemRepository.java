package com.example.pi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pi.model.CompraItem;

@Repository
public interface CompraItemRepository extends JpaRepository<CompraItem, Long> {

    List<CompraItem> findByCompra_Id(Long idCompra);
}