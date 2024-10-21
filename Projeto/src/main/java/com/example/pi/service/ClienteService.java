package com.example.pi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pi.model.Cliente;
import com.example.pi.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Cliente autenticar(String email, String senha) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente != null && isSenhaCorreta(senha, cliente.getSenha())) {
            return cliente;
        }
        return null;
    }

    private boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
        return passwordEncoder.matches(senhaDigitada, senhaArmazenada);
    }
}
