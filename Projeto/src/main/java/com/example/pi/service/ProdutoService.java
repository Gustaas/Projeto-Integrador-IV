package com.example.pi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pi.model.ImagemProduto;
import com.example.pi.model.Produto;
import com.example.pi.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public List<Produto> listarTodos() {
        System.out.println("Buscando todos os produtos...");
        List<Produto> produtos = produtoRepository.findAll();
        System.out.println("Produtos encontrados: " + produtos);
        return produtos;
    }

    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }

    public Produto alterarProduto(Long id, String nomeProduto, String descricao, Double preco, Integer qtd, Boolean ativo, Integer avaliacao) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setNomeProduto(nomeProduto);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setQtd(qtd);
        produto.setAtivo(ativo);
        produto.setAvaliacao(avaliacao);
        return produtoRepository.save(produto);
    }

    public Produto salvarProduto(Produto produto) {
        if (produto.getImagens() != null && !produto.getImagens().isEmpty()) {
            for (ImagemProduto imagem : produto.getImagens()) {
                imagem.setProduto(produto);
            }
        }

        return produtoRepository.save(produto);
    }

    public Produto alterarStatusProduto(Long id, Boolean ativo) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setAtivo(ativo);
        return produtoRepository.save(produto);
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

}
