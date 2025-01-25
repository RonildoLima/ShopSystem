package com.accenture.shopsystem.controllers.pedido;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.dtos.pedido.PedidoRequestDTO;
import com.accenture.shopsystem.repositories.PedidoRepository;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import com.accenture.shopsystem.producers.pedido.PedidoProducer;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos")
public class PedidoController {

    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ProdutoRepository produtoRepository;
    @Autowired
    VendedorRepository vendedorRepository;
    @Autowired
    PedidoProducer pedidoProducer;

    @PostMapping("/{vendedorId}")
    public ResponseEntity<String> salvar(@PathVariable String vendedorId, @RequestBody Pedido pedido) {
        // Verifica se o vendedor existe
        Vendedor vendedor = vendedorRepository.findById(vendedorId)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado: " + vendedorId));

        // Atualiza o vendedor no pedido
        pedido.setVendedor(vendedor);

        // Valida se todos os produtos existem no banco (mas não faz cálculos ou atualização de estoque)
        for (PedidoTemProdutos item : pedido.getProdutos()) {
            produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + item.getProduto().getId()));
        }

        // Envia o pedido para a fila do RabbitMQ
        pedidoProducer.enviarPedido(pedido);

        // Retorna uma mensagem de sucesso
        return ResponseEntity.ok("Pedido enviado para processamento!");
    }
}


