package com.example.pi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.pi.model.Produto;
import com.example.pi.model.Usuario;
import com.example.pi.repository.UsuarioRepository;
import com.example.pi.service.ProdutoService;
import com.example.pi.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "index";
    }

    @GetMapping("/lista-produto")
    public String listaProd() {
        List<Produto> produtos = produtoService.listarTodos();
        return "listaProduto";
    }

    @GetMapping("/cad-produto")
    public String cadProd() {
        return "cadprod";
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

    @PutMapping("/alterar-status/{id}")
    public ResponseEntity<String> alterarStatusUsuario(@PathVariable Long id, @RequestParam boolean ativo) {
        boolean sucesso = usuarioService.alterarStatusUsuario(id, ativo);
        if (sucesso) {
            String mensagem = ativo ? "Usuário ativado com sucesso." : "Usuário desabilitado com sucesso.";
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
    }

    @GetMapping("/listaUser")
    public String lista() {
        return "buscarUsuarios";
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
            return "buscarUsuarios";
        }

        if (!senha.equals(confirmSenha)) {
            model.addAttribute("erro", "As senhas não coincidem.");
            return "buscarUsuarios";
        }

        // Criptografar a senha com BCrypt
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setEmail(email);
        usuario.setSenha(new BCryptPasswordEncoder().encode(senha));

        String mensagem = usuarioService.salvarUsuario(usuario);
        model.addAttribute("mensagem", mensagem);

        return "redirect:/listaUser";
    }

    @PostMapping("/loginUser")
    public String loginUser(
            @RequestParam("email") String email,
            @RequestParam("senha") String senha,
            Model model,
            HttpSession session) {

        Usuario usuario = usuarioService.findByEmail(email);

        if (usuario != null) {
            System.out.println("Usuário encontrado: " + usuario.getEmail());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                System.out.println("Senha correta para o usuário: " + usuario.getEmail());
                session.setAttribute("tipoUsuario", usuario.getTipo());
                if (usuario.getTipo() == 1) {
                    return "redirect:/lista";
                } else if (usuario.getTipo() == 2) {
                    return "redirect:/lista";
                }
            } else {
                System.out.println("Senha incorreta para o usuário: " + usuario.getEmail());
                model.addAttribute("error", "Senha incorreta.");
                return "index";
            }
        } else {
            System.out.println("Usuário não encontrado com o e-mail: " + email);
        }

        model.addAttribute("error", "Usuário não encontrado.");
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
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
        return usuario != null ? usuario : new Usuario();
    }

    @PostMapping("/atualizarusuario")
    public String atualizarUsuario(
            @RequestParam("id") Long id,
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("tipo") int tipo,
            Model model) {

        Usuario usuario = usuarioService.findById(id);
        if (usuario != null) {
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setTipo(tipo);
            usuarioService.salvarUsuario(usuario);
            return "redirect:/lista";
        }

        model.addAttribute("erro", "Usuário não encontrado");
        return "redirect:/lista?erro=Usuário não encontrado";
    }
}
