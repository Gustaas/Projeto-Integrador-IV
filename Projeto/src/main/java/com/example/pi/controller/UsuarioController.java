package com.example.pi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pi.model.Usuario;
import com.example.pi.service.UsuarioService;

@Controller
@RequestMapping
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "index";
    }

    @PostMapping("/cadastrar")
    public String cadastrarUsuario(
            @RequestParam("nome") String nome,
            @RequestParam("cpf") String cpf,
            @RequestParam("email") String email,
            @RequestParam("senha") String senha) {

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        usuarioService.salvarUsuario(usuario);

        return "redirect:/";
    }
}
