package com.example.pi.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.pi.model.Usuario;
import com.example.pi.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private UsuarioRepository repository;
    private PasswordEncoder senhacript;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
        this.senhacript = new BCryptPasswordEncoder();
    }

    public Usuario salvarUsuario(Usuario usuario) {
        String encoder = this.senhacript.encode(usuario.getSenha());
        usuario.setSenha(encoder);
        Usuario novoUsuario = repository.save(usuario);
        return novoUsuario;
    }
}
