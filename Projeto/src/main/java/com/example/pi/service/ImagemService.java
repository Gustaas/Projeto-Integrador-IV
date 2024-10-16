package com.example.pi.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagemService {

    public String salvarImagem(MultipartFile imagem) {
        String nomeArquivo = imagem.getOriginalFilename();
        String caminho = "C:\\Imagens/" + nomeArquivo;

        File diretorio = new File("");
        if (!diretorio.exists()) {
            diretorio.mkdirs(); // Cria todos os diretórios necessários
        }

        try {
            // Salva a imagem no diretório especificado
            File file = new File(caminho);
            imagem.transferTo(file);
            return caminho; // Retorna o caminho onde a imagem foi salva
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar a imagem: " + e.getMessage(), e);
        }
    }
}
