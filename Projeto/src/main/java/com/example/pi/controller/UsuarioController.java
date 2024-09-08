package com.example.pi.controller;

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

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "index";
    }

    @GetMapping("/principal")
    public String principal() {
        return "principal";
    }

    @GetMapping("/listaUser")
    public String lista() {
        return "lista";
    }

    @PostMapping("/loginUser")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("senha") String senha,
            Model model) {
        
        Usuario usuario = usuarioService.login(email, senha);
        
        if (usuario != null) {
            if (usuario.getTipo() == 1) {
                return "redirect:/";
            } else if (usuario.getTipo() == 2) {
                return "redirect:/principal";
            }
        }

        model.addAttribute("error", "Usuário ou senha inválidos");
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
