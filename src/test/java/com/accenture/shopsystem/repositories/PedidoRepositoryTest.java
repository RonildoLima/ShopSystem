package com.accenture.shopsystem.repositories;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.repositories.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findPedidosByVendedorIdWithProdutos() {
        Vendedor vendedor = new Vendedor();
        vendedor.setEmail("vendedor@test.com");
        vendedor.setPassword("senha123");
        vendedor.setVendedorNome("Vendedor 1");
        vendedor.setVendedorSetor("Eletrônicos");
        vendedor = entityManager.persistAndFlush(vendedor);

        Produto produto = new Produto();
        produto.setProdutoDescricao("Produto 1");
        produto.setProdutoValor(new BigDecimal("100.00"));
        produto.setQuantidadeEstoque(10);
        produto.setVendedor(vendedor);
        produto = entityManager.persistAndFlush(produto);

        Pedido pedido = new Pedido();
        pedido.setPedidoDescricao("Pedido 1");
        pedido.setPedidoValor(new BigDecimal("200.00"));
        pedido.setPedidoQuantidade(2);
        pedido.setVendedor(vendedor);
        pedido = entityManager.persistAndFlush(pedido);

        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        pedidoTemProdutos.setPedido(pedido);
        pedidoTemProdutos.setProduto(produto);
        pedidoTemProdutos.setQuantidade(2);
        pedidoTemProdutos.setPrecoUnitario(new BigDecimal("100.00"));
        pedidoTemProdutos = entityManager.persistAndFlush(pedidoTemProdutos);

        pedido.getProdutos().add(pedidoTemProdutos);
        entityManager.persistAndFlush(pedido);

        List<Pedido> pedidos = pedidoRepository.findPedidosByVendedorIdWithProdutos(vendedor.getId());

        assertNotNull(pedidos);
        assertEquals(1, pedidos.size());

        Pedido retrievedPedido = pedidos.get(0);
        assertEquals("Pedido 1", retrievedPedido.getPedidoDescricao());
        assertEquals(1, retrievedPedido.getProdutos().size());

        PedidoTemProdutos retrievedPedidoTemProdutos = retrievedPedido.getProdutos().get(0);
        assertEquals(produto.getId(), retrievedPedidoTemProdutos.getProduto().getId());
        assertEquals(2, retrievedPedidoTemProdutos.getQuantidade());
        assertEquals(new BigDecimal("100.00"), retrievedPedidoTemProdutos.getPrecoUnitario());
    }

}
