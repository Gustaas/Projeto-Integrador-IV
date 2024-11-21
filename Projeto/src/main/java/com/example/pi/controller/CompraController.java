package com.example.pi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.pi.model.Compra;
import com.example.pi.model.CompraDTO;
import com.example.pi.service.CompraService;


@Controller
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private CompraService compraService;
    
    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }
    @GetMapping("/pagamentos")
    public String pagamentos() {
        return "pagamentos";
    }

    @PostMapping("/finalizar")
    public ResponseEntity<Compra> finalizarCompra(@RequestBody CompraDTO compraDTO) {
        Compra compra = compraService.finalizarCompra(compraDTO);
        System.out.println("Status enviado: " + compraDTO.getStatus());
        return ResponseEntity.ok(compra);
    }
}
