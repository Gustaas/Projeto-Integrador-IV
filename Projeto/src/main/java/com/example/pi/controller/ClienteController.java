package com.example.pi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.pi.model.Cliente;
import com.example.pi.model.LoginRequest;
import com.example.pi.repository.ClienteRepository;
import com.example.pi.repository.EnderecoRepository;
import com.example.pi.service.ClienteService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/cadastroUser")
    public String endereco() {
        return "cadastroUser";
    }

    @GetMapping("/loginUser")
    public String login() {
        return "loginUser";
    }

    @GetMapping("/listaEndereco")
    public String lista() {
        return "listaEndereco";
    }

    @GetMapping("/updateEndereco")
    public String update() {
        return "update";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Cliente cliente = clienteService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos.");
        }
        if (cliente.getTipo() == 3) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", cliente);
            return ResponseEntity.ok("Login bem-sucedido.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado para o tipo de cliente.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Map<String, String>> cadastrarCliente(@RequestBody Cliente cliente) {
        Map<String, String> response = new HashMap<>();

        if (clienteRepository.findByEmail(cliente.getEmail()) != null) {
            response.put("error", "E-mail já cadastrado.");
            return ResponseEntity.badRequest().body(response);
        }
        if (clienteRepository.findByCpf(cliente.getCpf()) != null) {
            response.put("error", "CPF já cadastrado.");
            return ResponseEntity.badRequest().body(response);
        }

        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));

        if (cliente.getEnderecos() != null) {
            cliente.getEnderecos().forEach(endereco -> {
                endereco.setCliente(cliente);
            });
        }

        clienteRepository.save(cliente);
        response.put("message", "Cliente cadastrado com sucesso!");
        return ResponseEntity.ok(response);
    }

}
