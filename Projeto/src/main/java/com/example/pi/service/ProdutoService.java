package com.example.pi.service;

import com.example.pi.model.Produto;
import com.example.pi.repository.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public void salvarProduto(Produto produto) {
        produtoRepository.save(produto);
        System.out.println("Produto salvo: " + produto.getNomeProduto());       
    }
    

    public Produto alterarProduto(Long id, String nomeProduto, String descricao, Double preco, Integer qtd, Boolean ativo,
            String imagem, Integer avaliacao) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setNomeProduto(nomeProduto);
        produto.setDesc(descricao);
        produto.setPreco(preco);
        produto.setQtd(qtd);
        produto.setAtivo(ativo);
        produto.setImagem(imagem);
        produto.setAvaliacao(avaliacao);
        return produtoRepository.save(produto);
    }

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

}