package com.example.pi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.pi.model.ImagemProduto;
import com.example.pi.model.Produto;
import com.example.pi.repository.ProdutoRepository;
import com.example.pi.service.ImagemService;
import com.example.pi.service.ProdutoService;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ImagemService imagemService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/produtos")
    @ResponseBody
    public List<Produto> listarProdutosApi() {
        return produtoService.listarProdutos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.findById(id);
        if (produto.isPresent()) {
            return ResponseEntity.ok(produto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listarProd")
    public String listarProds() {
        return "produtos-index";
    }

    @GetMapping("/detalhesProduto")
    public String detalhes() {
        return "detalhes-produto";
    }

    @PutMapping("/alterar-status/{id}")
    public ResponseEntity<String> alterarStatusProduto(@PathVariable Long id, @RequestBody Map<String, Boolean> status) {
        Boolean ativo = status.get("ativo");
        produtoService.alterarStatusProduto(id, ativo);
        return ResponseEntity.ok("Status do produto atualizado com sucesso");
    }

    @GetMapping("/carrinho")
    public String carrinho() {
        return "carrinho";
    }

    @GetMapping("/listar")
    public String listarProdutos(Model model) {
        List<Produto> produtos = produtoService.listarProdutos();
        model.addAttribute("produtos", produtos);
        return "listaProduto";
    }

    @GetMapping("/listarProdutosJson")
    public ResponseEntity<List<Produto>> listarProdutosJson() {
        try {
            List<Produto> produtos = produtoService.listarTodos();
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            // Log do erro
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/alterarProduto")
    @ResponseBody
    public ResponseEntity<Produto> alterarProduto(@RequestBody Produto produto) {
        Produto produtoAlterado = produtoService.alterarProduto(
                produto.getId(),
                produto.getNomeProduto(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQtd(),
                produto.isAtivo(),
                produto.getAvaliacao());

        return ResponseEntity.ok(produtoAlterado);
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);

        Produto produtoAtualizado = produtoExistente.get();
        produtoAtualizado.setNomeProduto(produto.getNomeProduto());
        produtoAtualizado.setDescricao(produto.getDescricao());
        produtoAtualizado.setPreco(produto.getPreco());
        produtoAtualizado.setQtd(produto.getQtd());
        produtoAtualizado.setAtivo(produto.isAtivo());
        produtoAtualizado.setAvaliacao(produto.getAvaliacao());

        produtoRepository.save(produtoAtualizado);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Produto> cadastrarProduto(
            @RequestParam("nomeProduto") String nomeProduto,
            @RequestParam("descricao") String descricao,
            @RequestParam("preco") double preco,
            @RequestParam("qtd") Integer qtd,
            @RequestParam("avaliacao") Integer avaliacao,
            @RequestParam("ativo") Boolean ativo,
            @RequestParam("imagens") List<MultipartFile> imagens) {

        try {
            Produto produto = new Produto();
            produto.setNomeProduto(nomeProduto);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setQtd(qtd);
            produto.setAvaliacao(avaliacao);
            produto.setAtivo(ativo);

            // Salvar o produto primeiro
            produto = produtoRepository.save(produto);

            // Agora que o produto foi salvo e tem um ID, associamos as imagens
            List<ImagemProduto> listaImagens = new ArrayList<>();
            for (MultipartFile imagem : imagens) {
                String link = imagemService.salvarImagem(imagem);
                ImagemProduto novaImagem = new ImagemProduto();
                novaImagem.setProduto(produto); // Associando a imagem ao produto
                novaImagem.setLink(link); // Definindo o link da imagem
                listaImagens.add(novaImagem);
            }

            // Salvar as imagens no banco de dados, se houver um repositório para ImagemProduto
            // Se necessário, você pode ter um repositório de imagem e salvá-las aqui
            // imagemProdutoRepository.saveAll(listaImagens); // Descomente se houver repositório
            produto.setImagens(listaImagens); // Se a associação for bidirecional
            return ResponseEntity.status(HttpStatus.CREATED).body(produto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
