package com.example.pi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.pi.model.Cliente;
import com.example.pi.model.Endereco;
import com.example.pi.model.LoginRequest;
import com.example.pi.repository.ClienteRepository;
import com.example.pi.repository.EnderecoRepository;
import com.example.pi.service.ClienteService;
import com.example.pi.service.EnderecoService;
import com.example.pi.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class ClienteController {

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioService usuarioService;

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

    @GetMapping("/enderecos/{id}")
    public ResponseEntity<List<Endereco>> buscarEnderecos(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Endereco> enderecos = enderecoService.buscarPorClienteId(cliente.getId());
        return ResponseEntity.ok(enderecos);
    }

    @PostMapping("/endereco")
    public ResponseEntity<String> adicionarEndereco(@RequestBody Endereco endereco, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogado");

        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }

        endereco.setCliente(cliente);
        enderecoService.salvarEndereco(endereco);

        return ResponseEntity.ok("Endereço adicionado com sucesso.");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Cliente cliente = clienteService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos.");
        }
        if (cliente.getTipo() == 3) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", cliente);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login bem-sucedido.");
            response.put("userId", cliente.getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado para o tipo de cliente.");
        }
    }

    @GetMapping("/dados/{id}")
    public ResponseEntity<Cliente> obterDadosCliente(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarClientePorId(id);
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(cliente);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/enderecos/principal/{userId}/{enderecoId}")
    public ResponseEntity<Void> setPrincipalAddress(@PathVariable Long userId, @PathVariable Long enderecoId) {
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(enderecoId);
        if (enderecoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Endereco endereco = enderecoOptional.get();
        if (!endereco.getCliente().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        enderecoRepository.updatePrincipalStatus(userId, false);
        endereco.setPrincipal(true);
        enderecoRepository.save(endereco);
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

    @PostMapping("/endereco/adicionar/{clienteId}")
    public ResponseEntity<Endereco> adicionarEndereco(@PathVariable Long clienteId, @RequestBody Endereco endereco) {
        Endereco enderecoSalvo = enderecoService.salvarEndereco(clienteId, endereco);
        return ResponseEntity.ok(enderecoSalvo);
    }

}
