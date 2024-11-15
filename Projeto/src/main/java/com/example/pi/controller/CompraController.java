package com.example.pi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/compra")
public class CompraController {
    
    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }
}
