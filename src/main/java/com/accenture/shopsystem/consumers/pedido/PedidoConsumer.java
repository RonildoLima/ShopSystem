package com.accenture.shopsystem.consumers.pedido;

import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class PedidoConsumer {

    @Autowired
    private PedidoRepository pedidoRepository;
    private ProdutoRepository produtoRepository;
    private PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository;

    @Autowired
    public PedidoConsumer(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository, PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoHistoricoStatusRepository = pedidoHistoricoStatusRepository;
    }

    @RabbitListener(queues = "pedido-queue")
    public void processarPedido(Pedido pedido) {
        try {
            // Busca e valida todos os produtos antes de qualquer atualização
            Map<String, Produto> produtosMap = new HashMap<>();
            for (PedidoTemProdutos item : pedido.getProdutos()) {
                Produto produto = produtoRepository.findById(item.getProduto().getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + item.getProduto().getId()));

                if (produto.getQuantidadeEstoque() < item.getQuantidade()) {
                    throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getProdutoDescricao());
                }

                produtosMap.put(item.getProduto().getId(), produto);
            }

            // Se todos os produtos forem válidos, inicia o processamento
            List<PedidoTemProdutos> itensAtualizados = new ArrayList<>();
            BigDecimal valorTotal = BigDecimal.ZERO;
            int quantidadeTotal = 0;

            for (PedidoTemProdutos item : pedido.getProdutos()) {
                Produto produto = produtosMap.get(item.getProduto().getId());

                // Atualiza o estoque
                produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
                produtoRepository.save(produto);

                // Calcula o valor total do item
                BigDecimal valorItem = produto.getProdutoValor().multiply(BigDecimal.valueOf(item.getQuantidade()));

                // Cria uma nova instância de PedidoTemProdutos
                PedidoTemProdutos itemAtualizado = new PedidoTemProdutos();
                itemAtualizado.setProduto(produto);
                itemAtualizado.setQuantidade(item.getQuantidade());
                itemAtualizado.setPrecoUnitario(produto.getProdutoValor());
                itemAtualizado.setPedido(pedido);

                // Atualiza os valores totais do pedido
                valorTotal = valorTotal.add(valorItem);
                quantidadeTotal += item.getQuantidade();

                // Adiciona o item atualizado à lista
                itensAtualizados.add(itemAtualizado);
            }

            // Atualiza os valores do pedido
            pedido.setProdutos(itensAtualizados);
            pedido.setPedidoValor(valorTotal);
            pedido.setPedidoQuantidade(quantidadeTotal);

            // Salva o pedido no banco de dados
            Pedido pedidoSalvo = pedidoRepository.save(pedido);

            // Processa o histórico de status
            PedidoHistoricoStatus historicoStatus = new PedidoHistoricoStatus ();
            historicoStatus.setPedido(pedidoSalvo);
            historicoStatus.setStatusPedido(StatusPedidoEnum.PENDENTE);
            historicoStatus.setDataHoraStatusPedido(LocalDateTime.now());
            historicoStatus.setDataHoraPagamento(null);

            pedidoHistoricoStatusRepository.save(historicoStatus);

            System.out.println("Pedido processado com sucesso: " + pedidoSalvo.getId());

        } catch (Exception e) {
            System.err.println("Erro ao processar pedido: " + e.getMessage());
        }
    }


}


