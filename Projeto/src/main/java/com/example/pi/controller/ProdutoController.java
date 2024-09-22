package com.example.pi.controller;

import com.example.pi.model.Produto;
import com.example.pi.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/listar")
    public String listarProdutos(Model model) {
        List<Produto> produtos = produtoService.listarProdutos();
        model.addAttribute("produtos", produtos);
        return "listaProduto";
    }

    @GetMapping("/listarProdutosJson")
    @ResponseBody
    public List<Produto> listarProdutosJson() {
        return produtoService.listarProdutos();
    }

    @PostMapping("/cadastrar-produto")
public String cadastrarProduto(
        @RequestParam("product-name") String nome,
        @RequestParam("product-price") double preco,
        @RequestParam("product-stock") int estoque,
        @RequestParam("product-description") String descricao,
        @RequestParam("product-rating") int avaliacao,
        @RequestParam("product-images") MultipartFile[] imagens,
        Model model) {

    Produto novoProduto = new Produto();
    novoProduto.setNomeProduto(nome);
    novoProduto.setPreco(preco);
    novoProduto.setQtd(estoque);
    novoProduto.setDesc(descricao);
    novoProduto.setAvaliacao(avaliacao);
    
    return "redirect:/produtos/listar"; }



    @PostMapping("/alterarProduto")
    @ResponseBody
    public ResponseEntity<Produto> alterarProduto(@RequestBody Produto produto) {
        Produto produtoAlterado = produtoService.alterarProduto(
                produto.getId(),
                produto.getNomeProduto(),
                produto.getDesc(),
                produto.getPreco(),
                produto.getQtd(),
                produto.isAtivo(),
                produto.getImagem(),
                produto.getAvaliacao()
        );

        return ResponseEntity.ok(produtoAlterado);
    }

}