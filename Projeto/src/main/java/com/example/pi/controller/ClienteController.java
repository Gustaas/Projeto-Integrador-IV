package com.example.pi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.pi.model.Cliente;
import com.example.pi.repository.ClienteRepository;
import com.example.pi.repository.EnderecoRepository;

@Controller
@RequestMapping("/user")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/cadastroUser")
    public String endereco() {
        return "cadastroUser";
    }

    @GetMapping("/listaEndereco")
    public String lista() {
        return "listaEndereco";
    }

    @GetMapping("/updateEndereco")
    public String update() {
        return "update";
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
