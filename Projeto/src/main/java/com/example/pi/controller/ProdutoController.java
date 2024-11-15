package com.example.pi.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import com.example.pi.repository.ImagemProdutoRepository;
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

    @Autowired
    private ImagemProdutoRepository imagemRepository;

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

    @GetMapping("/detalhesUser")
    public String detalesUser() {
        return "detalhesCliente";
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
    @ResponseBody
    public List<Produto> listarProdutosJson() {
        List<Produto> produtos = produtoService.listarTodos();
        for (Produto produto : produtos) {
            for (ImagemProduto imagem : produto.getImagens()) {
                imagem.setProduto(null);
            }
        }
        return produtos;
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
        if (!produtoExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

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

    @GetMapping("/{id}/imagens")
    @ResponseBody
    public ResponseEntity<List<ImagemProduto>> buscarImagensPorProdutoId(@PathVariable Long id) {
        List<ImagemProduto> imagens = imagemService.buscarPorProdutoId(id);

        if (!imagens.isEmpty()) {
            for (ImagemProduto imagem : imagens) {
                // Supondo que o link original é algo como "C:\\...\\img\\exemplo de nome"
                String linkOriginal = imagem.getLink();
                String nomeImagem = new File(linkOriginal).getName();
                // Aqui, assumimos que as imagens são acessíveis através do caminho relativo
                imagem.setLink("/img/" + nomeImagem); // Ajuste para o caminho correto
                imagem.setProduto(null);
            }
            return ResponseEntity.ok(imagens);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/imagens/{nomeImagem}")
    public ResponseEntity<Resource> servirImagem(@PathVariable String nomeImagem) {
        try {
            Path caminhoImagem = Paths.get("C:\\Users\\conta\\Desktop\\Projeto-Integrador-IV\\Projeto\\src\\main\\resources\\static\\img").resolve(nomeImagem);
            Resource imagem = new UrlResource(caminhoImagem.toUri());

            if (imagem.exists() || imagem.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imagem.getFilename() + "\"")
                        .body(imagem);
            } else {
                throw new RuntimeException("Imagem não encontrada ou não é legível.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Produto> cadastrarProduto(
            @RequestParam("nomeProduto") String nomeProduto,
            @RequestParam("descricao") String descricao,
            @RequestParam("preco") double preco,
            @RequestParam("qtd") Integer qtd,
            @RequestParam("avaliacao") Integer avaliacao,
            @RequestParam("ativo") Boolean ativo,
            @RequestParam("imagens") List<MultipartFile> imagens,
            @RequestParam(value = "imagemPrincipal", required = false) String imagemPrincipal) { // Mudança para String

        try {
            Produto produto = new Produto();
            produto.setNomeProduto(nomeProduto);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setQtd(qtd);
            produto.setAvaliacao(avaliacao);
            produto.setAtivo(ativo);

            List<ImagemProduto> listaImagens = new ArrayList<>();
            for (MultipartFile imagem : imagens) {
                String link = imagemService.salvarImagem(imagem); // Salva a imagem e obtém o link
                ImagemProduto novaImagem = new ImagemProduto();
                novaImagem.setLink(link);
                novaImagem.setProduto(produto);
                // Verifica se a imagem atual é a principal
                if (imagemPrincipal != null && imagemPrincipal.equals(imagem.getOriginalFilename())) {
                    novaImagem.setPrincipal(true); // Define como principal
                } else {
                    novaImagem.setPrincipal(false); // Define como não principal
                }
                listaImagens.add(novaImagem);
            }

            produto.setImagens(listaImagens);
            Produto produtoSalvo = produtoRepository.save(produto); // Salva o produto no banco

            // Salva as imagens associadas ao produto no banco
            imagemRepository.saveAll(listaImagens); // Salva todas as imagens

            return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
