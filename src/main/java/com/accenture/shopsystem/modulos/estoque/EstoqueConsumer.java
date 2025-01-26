package com.accenture.shopsystem.modulos.estoque;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EstoqueConsumer {

    @Autowired
    ProdutoRepository produtoRepository;

    @RabbitListener(queues = "estoque-queue")
    public void atualizarEstoque(Pedido pedido) {
        try {
            for (PedidoTemProdutos item : pedido.getProdutos()) {
                Produto produto = produtoRepository.findById(item.getProduto().getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + item.getProduto().getId()));

                if (produto.getQuantidadeEstoque() < item.getQuantidade()) {
                    throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getProdutoDescricao());
                }

                produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
                produtoRepository.save(produto);
            }

            System.out.println("Estoque atualizado com sucesso para o pedido: " + pedido.getId());
        } catch (Exception e) {
            System.err.println("Erro ao atualizar estoque: " + e.getMessage());
        }
    }
}
