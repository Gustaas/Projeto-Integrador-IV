package com.example.pi.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @GetMapping("/login")
    public String login() {
        return "index";
    }
}