package com.example.pi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pi.model.Cliente;
import com.example.pi.model.Endereco;
import com.example.pi.model.EnderecoDTO;
import com.example.pi.repository.ClienteRepository;
import com.example.pi.repository.EnderecoRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Cliente autenticar(String email, String senha) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente != null && isSenhaCorreta(senha, cliente.getSenha())) {
            return cliente;
        }
        return null;
    }

    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    private EnderecoDTO convertToEnderecoDTO(Endereco endereco) {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setLogradouro(endereco.getLogradouro());
        enderecoDTO.setNumero(endereco.getNumero());
        enderecoDTO.setComplemento(endereco.getComplemento());
        enderecoDTO.setBairro(endereco.getBairro());
        enderecoDTO.setCidade(endereco.getCidade());
        enderecoDTO.setUf(endereco.getUf());
        enderecoDTO.setCep(endereco.getCep());
        enderecoDTO.setPrincipal(endereco.isPrincipal());

        return enderecoDTO;
    }

    public Cliente findById(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    private boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
        return passwordEncoder.matches(senhaDigitada, senhaArmazenada);
    }
}
