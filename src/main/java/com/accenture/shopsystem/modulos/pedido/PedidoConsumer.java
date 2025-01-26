package com.accenture.shopsystem.modulos.pedido;

import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public PedidoConsumer(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository,
                          PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository, RabbitTemplate rabbitTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoHistoricoStatusRepository = pedidoHistoricoStatusRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "pedido-queue")
    public void processarPedido(Pedido pedido) {
        try {
            Map<String, Produto> produtosMap = new HashMap<>();
            for (PedidoTemProdutos item : pedido.getProdutos()) {
                Produto produto = produtoRepository.findById(item.getProduto().getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + item.getProduto().getId()));

                if (produto.getQuantidadeEstoque() < item.getQuantidade()) {
                    throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getProdutoDescricao());
                }

                produtosMap.put(item.getProduto().getId(), produto);
            }

            List<PedidoTemProdutos> itensAtualizados = new ArrayList<>();
            BigDecimal valorTotal = BigDecimal.ZERO;
            int quantidadeTotal = 0;

            for (PedidoTemProdutos item : pedido.getProdutos()) {
                Produto produto = produtosMap.get(item.getProduto().getId());

                BigDecimal valorItem = produto.getProdutoValor().multiply(BigDecimal.valueOf(item.getQuantidade()));

                PedidoTemProdutos itemAtualizado = new PedidoTemProdutos();
                itemAtualizado.setProduto(produto);
                itemAtualizado.setQuantidade(item.getQuantidade());
                itemAtualizado.setPrecoUnitario(produto.getProdutoValor());
                itemAtualizado.setPedido(pedido);

                valorTotal = valorTotal.add(valorItem);
                quantidadeTotal += item.getQuantidade();

                itensAtualizados.add(itemAtualizado);
            }

            pedido.setProdutos(itensAtualizados);
            pedido.setPedidoValor(valorTotal);
            pedido.setPedidoQuantidade(quantidadeTotal);
            Pedido pedidoSalvo = pedidoRepository.save(pedido);

            PedidoHistoricoStatus historicoStatus = new PedidoHistoricoStatus();
            historicoStatus.setPedido(pedidoSalvo);
            historicoStatus.setStatusPedido(StatusPedidoEnum.PENDENTE);
            LocalDateTime dataHoraStatus = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String dataHoraFormatada = dataHoraStatus.format(formatter);
            historicoStatus.setDataHoraStatusPedido(LocalDateTime.parse(dataHoraFormatada, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            historicoStatus.setDataHoraPagamento(null);

            pedidoHistoricoStatusRepository.save(historicoStatus);

            // Enviar dados para a fila estoque-queue
            rabbitTemplate.convertAndSend("estoque-queue", pedido);

            System.out.println("Pedido processado com sucesso: " + pedidoSalvo.getId());
        } catch (Exception e) {
            System.err.println("Erro ao processar pedido: " + e.getMessage());
        }
    }
}
