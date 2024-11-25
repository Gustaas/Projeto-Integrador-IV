package com.example.pi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pi.model.ImagemProduto;
import com.example.pi.repository.ImagemProdutoRepository;

@Service
public class ImagemService {

    @Autowired
    private ImagemProdutoRepository imagemProdutoRepository;

    public String salvarImagem(MultipartFile imagem) {
        try {
            // Cria um nome único para a imagem (opcional)
            String nomeImagem = imagem.getOriginalFilename(); // Ou pode usar UUID para nome único
            Path caminho = Paths.get("src/main/resources/static/img/" + nomeImagem); // Caminho onde a imagem será salva
            Files.copy(imagem.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING); // Salva a imagem
            return nomeImagem; // Retorna apenas o nome da imagem
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar a imagem");
        }
    }

    public List<ImagemProduto> buscarPorProdutoId(Long idProduto) {
        return imagemProdutoRepository.findByProdutoId(idProduto);
    }
}
