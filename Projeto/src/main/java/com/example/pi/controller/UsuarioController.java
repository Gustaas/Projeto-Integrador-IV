package com.example.pi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.pi.model.Usuario;
import com.example.pi.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

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

    @GetMapping("/lista")
    public String dashboard(HttpSession session, Model model) {
        Integer tipoUsuario = (Integer) session.getAttribute("tipoUsuario");
        if (tipoUsuario != null) {
            model.addAttribute("tipoUsuario", tipoUsuario);
            return "principal";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/listaUser")
    public String lista() {
        return "buscarUsuarios";
    }

    @PostMapping("/loginUser")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("senha") String senha,
            HttpSession session,
            Model model) {

        Usuario usuario = usuarioService.login(email, senha);

        if (usuario != null) {
            session.setAttribute("tipoUsuario", usuario.getTipo());

            if (usuario.getTipo() == 1) {
                return "redirect:/";
            } else if (usuario.getTipo() == 2 || usuario.getTipo() == 3) {
                return "redirect:/lista";
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
            @RequestParam("senha") String senha,
            @RequestParam("ConfirmPassword") String confirmSenha,
            Model model) {
        if (!usuarioService.validarCPF(cpf)) {
            model.addAttribute("erro", "CPF inválido.");
            return "index";
        }

        if (!senha.equals(confirmSenha)) {
            model.addAttribute("erro", "As senhas não coincidem.");
            return "index";
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setEmail(email);
        usuario.setSenha(new BCryptPasswordEncoder().encode(senha)); // Criptografa a senha

        String mensagem = usuarioService.salvarUsuario(usuario);
        model.addAttribute("mensagem", mensagem);

        return "redirect:/";
    }

    @GetMapping("/buscarusuarios")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/usuario/{id}")
    @ResponseBody
    public Usuario getUserById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        return usuario != null ? usuario : new Usuario(); // Retorna um novo usuário se não encontrar
    }

    @PostMapping("/atualizarusuario")
    public String atualizarUsuario(
            @RequestParam("id") Long id,
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("ativo") boolean ativo,
            @RequestParam("tipo") int tipo,
            Model model) {

        Usuario usuario = usuarioService.findById(id); // Encontre o usuário por ID
        if (usuario != null) {
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setAtivo(ativo);
            usuario.setTipo(tipo);
            usuarioService.salvarUsuario(usuario);
            return "redirect:/lista"; // Redireciona para a lista de usuários após a atualização
        }

        model.addAttribute("erro", "Usuário não encontrado");
        return "redirect:/lista?erro=Usuário não encontrado"; // Redireciona para a lista com uma mensagem de erro
    }
}
