package com.example.pi.service;

import java.util.Optional;

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

    public Usuario login(String email, String senha) {
        Optional<Usuario> usuarioOpt = repository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (senhacript.matches(senha, usuario.getSenha())) {
                return usuario;
            }
        }
        return null;
    }
}
