package com.accenture.shopsystem.controllers.pedido;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.repositories.PedidoRepository;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import com.accenture.shopsystem.services.pedido.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    PedidoService pedidoService;

    @PostMapping("/{vendedorId}")
    @Operation(summary = "Inserir pedido", description = "Enviar novo pedido para processamento")
    public ResponseEntity<String> salvar(@PathVariable String vendedorId, @RequestBody Pedido pedido) {
        Vendedor vendedor = vendedorRepository.findById (vendedorId)
                .orElseThrow (() -> new RuntimeException ("Vendedor não encontrado: " + vendedorId));
        pedido.setVendedor (vendedor);

        for (PedidoTemProdutos item : pedido.getProdutos ()) {
            produtoRepository.findById (item.getProduto ().getId ())
                    .orElseThrow (() -> new RuntimeException ("Produto não encontrado: " + item.getProduto ().getId ()));
        }

        pedidoService.enviarPedido (pedido);
        return ResponseEntity.ok ("Pedido enviado para processamento!");
    }
    @Operation(summary = "Deletar pedido", description = "Deletar um pedido do banco de dados")
    @DeleteMapping("/{pedidoId}/vendedor/{vendedorId}")
    public ResponseEntity<Void> deletarPedido(
            @PathVariable("pedidoId") String pedidoId,
            @PathVariable("vendedorId") String vendedorId
    ) {
        pedidoService.deletarPedido(pedidoId, vendedorId);
        return ResponseEntity.noContent().build();
    }
}


