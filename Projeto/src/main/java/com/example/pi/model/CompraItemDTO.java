package com.example.pi.model;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CompraItemDTO {
    
    @NotNull
    private Long idProduto;

    @Min(value = 1, message = "Quantidade deve ser maior que 0")
    private Integer quantidade;

    // Getters e Setters
    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}