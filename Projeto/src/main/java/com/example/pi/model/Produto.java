package com.example.pi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nomeProduto", nullable = false) // Adicionando nullable para evitar valores nulos
    private String nomeProduto;

    @Column(name = "avaliacao")
    private int avaliacao;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "preco", nullable = false) // Adicionando nullable
    private double preco;

    @Column(name = "qtd", nullable = false) // Adicionando nullable
    private int qtd;

    @Column(name = "ativo")
    private boolean ativo;

    @Column(name = "imagem")
    private String imagem;

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getDesc() {
        return descricao;
    }

    public void setDesc(String desc) {
        this.descricao = desc;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
