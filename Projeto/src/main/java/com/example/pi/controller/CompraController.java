package com.example.pi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.pi.model.Compra;
import com.example.pi.model.CompraDTO;
import com.example.pi.model.CompraItemDTO;
import com.example.pi.repository.CompraRepository;
import com.example.pi.service.CompraService;
import com.example.pi.service.ImagemService;

@Controller
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private ImagemService imagemProdutoService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private CompraRepository compraRepository;

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/estoque")
    public String estoque() {
        return "estoque";
    }

    @GetMapping("/pagamentos")
    public String pagamentos() {
        return "pagamentos";
    }

    @GetMapping("/buscarCompras/{idCliente}")
    public ResponseEntity<List<CompraDTO>> getComprasByCliente(@PathVariable Long idCliente) {
        List<Compra> compras = compraRepository.findByClienteIdWithItens(idCliente);

        if (compras.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Converter lista de `Compra` para lista de `CompraDTO`
        List<CompraDTO> comprasDTO = compras.stream().map(compra -> {
            CompraDTO compraDTO = new CompraDTO();
            compraDTO.setNumeroPedido(compra.getNumeroPedido());
            compraDTO.setValorTotal(compra.getValorTotal());
            compraDTO.setStatus(compra.getStatus().getDescricao());
            compraDTO.setIdCliente(compra.getCliente().getId());
            compraDTO.setIdEndereco(compra.getEndereco().getId());
            compraDTO.setTipoFrete(compra.getTipoFrete());
            compraDTO.setFormaPagamento(compra.getFormaPagamento());
            compraDTO.setParcelas(compra.getParcelas());
            compraDTO.setValorParcelas(compra.getValorParcelas());

            // Mapear os itens
            List<CompraItemDTO> itensDTO = compra.getItens().stream()
                    .map(item -> {
                        CompraItemDTO itemDTO = new CompraItemDTO();
                        itemDTO.setIdProduto(item.getProduto().getId());
                        itemDTO.setQuantidade(item.getQuantidade());
                        return itemDTO;
                    })
                    .collect(Collectors.toList());

            compraDTO.setItens(itensDTO);
            return compraDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(comprasDTO);
    }

    @GetMapping("/buscarCompras")
public ResponseEntity<List<CompraDTO>> getTodasCompras() {
    // Buscar todas as compras no banco de dados
    List<Compra> compras = compraRepository.findAll();

    if (compras.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    // Converter lista de `Compra` para lista de `CompraDTO`
    List<CompraDTO> comprasDTO = compras.stream().map(compra -> {
        CompraDTO compraDTO = new CompraDTO();
        compraDTO.setNumeroPedido(compra.getNumeroPedido());
        compraDTO.setValorTotal(compra.getValorTotal());
        compraDTO.setStatus(compra.getStatus().getDescricao());
        compraDTO.setIdCliente(compra.getCliente().getId());
        compraDTO.setIdEndereco(compra.getEndereco().getId());
        
        // Verificando e ajustando o tipoFrete e formaPagamento
        String tipoFrete = compra.getTipoFrete();
        if (tipoFrete != null && !tipoFrete.isEmpty()) {
            compraDTO.setTipoFrete(tipoFrete);
        } else {
            compraDTO.setTipoFrete("Não especificado");
        }

        String formaPagamento = compra.getFormaPagamento();
        if (formaPagamento != null && !formaPagamento.isEmpty()) {
            compraDTO.setFormaPagamento(formaPagamento);
        } else {
            compraDTO.setFormaPagamento("Não especificado");
        }

        compraDTO.setParcelas(compra.getParcelas());
        compraDTO.setValorParcelas(compra.getValorParcelas());

        // Mapear os itens
        List<CompraItemDTO> itensDTO = compra.getItens().stream()
                .map(item -> {
                    CompraItemDTO itemDTO = new CompraItemDTO();
                    itemDTO.setIdProduto(item.getProduto().getId());
                    itemDTO.setQuantidade(item.getQuantidade());
                    return itemDTO;
                })
                .collect(Collectors.toList());

        compraDTO.setItens(itensDTO);
        return compraDTO;
    }).collect(Collectors.toList());

    return ResponseEntity.ok(comprasDTO);
}


    @PostMapping("/finalizar")
    public ResponseEntity<Compra> finalizarCompra(@RequestBody CompraDTO compraDTO) {
        Compra compra = compraService.finalizarCompra(compraDTO);
        System.out.println("Status enviado: " + compraDTO.getStatus());
        return ResponseEntity.ok(compra);
    }

}
