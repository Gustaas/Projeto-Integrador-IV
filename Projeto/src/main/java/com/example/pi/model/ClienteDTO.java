package com.example.pi.model;

import java.util.List;

public class ClienteDTO {

    private Long id;  // Alterado de long para Long
    private String nome;
    private String cpf;
    private String email;
    private String dataNascimento;
    private String genero;
    private List<EnderecoDTO> enderecos;

    // Construtor
    public ClienteDTO(Long id, String nome, String cpf, String email, String dataNascimento, String genero, List<EnderecoDTO> enderecos) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.enderecos = enderecos;
    }


    


    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {  // Alterado para Long
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public List<EnderecoDTO> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoDTO> enderecos) {
        this.enderecos = enderecos;
    }
}
