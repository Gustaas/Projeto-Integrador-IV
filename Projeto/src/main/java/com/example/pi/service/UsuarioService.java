package com.example.pi.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.pi.model.Usuario;
import com.example.pi.repository.UsuarioRepository;
import org.slf4j.Logger;

@Service
public class UsuarioService {

    private UsuarioRepository repository;
    private PasswordEncoder senhacript;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
        this.senhacript = new BCryptPasswordEncoder();
    }

    public List<Usuario> buscarUsuarios(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return repository.findAll();
        } else {
            return repository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(searchTerm, searchTerm);
        }
    }

    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = repository.findAll();
        if (usuarios.isEmpty()) {
            logger.info("Nenhum usuário encontrado.");
        } else {
            logger.info("Usuários encontrados: {}", usuarios);
        }
        return usuarios;
    }

    public boolean validarCPF(String cpf) {
        final String cpfLocal = cpf.replaceAll("[^\\d]", ""); // Use uma variável final ou efetivamente final

        if (cpfLocal.length() != 11 || cpfLocal.chars().allMatch(c -> c == cpfLocal.charAt(0))) {
            return false;
        }

        int soma = 0;
        int digito;
        for (int i = 0; i < 9; i++) {
            soma += (cpfLocal.charAt(i) - '0') * (10 - i);
        }
        digito = 11 - (soma % 11);
        if (digito >= 10)
            digito = 0;
        if (digito != (cpfLocal.charAt(9) - '0'))
            return false;

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpfLocal.charAt(i) - '0') * (11 - i);
        }
        digito = 11 - (soma % 11);
        if (digito >= 10)
            digito = 0;
        return digito == (cpfLocal.charAt(10) - '0');
    }

    public Usuario findByEmail(String email) {
        Optional<Usuario> usuarioOpt = repository.findByEmail(email);
        return usuarioOpt.orElse(null);
    }

    public String salvarUsuario(Usuario usuario) {
        if (validarCPF(usuario.getCpf())) {
            repository.save(usuario);
            return "Usuário cadastrado com sucesso";
        } else {
            return "CPF inválido";
        }
    }

    public Usuario findById(Long id) {
        return repository.findById(id).orElse(null);
    }

}
