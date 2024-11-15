package com.example.pi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pi.model.Produto;
import com.example.pi.repository.ProdutoRepository;

@Service
public class CompraService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> getProdutosCarrinho() {
        return produtoRepository.findAll();
    }

    public Double calcularTotal(List<Produto> produtos) {
        double total = 0;
        for (Produto produto : produtos) {
            total += produto.getPreco();
        }
        return total;
    }
}
