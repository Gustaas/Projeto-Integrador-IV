package com.example.pi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pi.model.Cliente;
import com.example.pi.model.Endereco;
import com.example.pi.repository.ClienteRepository;
import com.example.pi.repository.EnderecoRepository;

import jakarta.transaction.Transactional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public void salvarEndereco(Endereco endereco) {
        enderecoRepository.save(endereco);
    }

    public List<Endereco> buscarPorClienteId(Long clienteId) {
        return enderecoRepository.findByClienteId(clienteId);
    }

    public Endereco salvarEndereco(Long clienteId, Endereco endereco) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (endereco.isPrincipal()) {
            cliente.getEnderecos().forEach(e -> e.setPrincipal(false));
        }

        endereco.setCliente(cliente);
        return enderecoRepository.save(endereco);
    }

    @Transactional
    public void atualizarEndereco(Long enderecoId, boolean principal) {
        enderecoRepository.updatePrincipalStatus(enderecoId, principal);
    }
    
    public Optional<Endereco> getEnderecoById(Long id) {
        return enderecoRepository.findById(id);
    }

    public List<Endereco> findAll() {
        return enderecoRepository.findAll();
    }

}
