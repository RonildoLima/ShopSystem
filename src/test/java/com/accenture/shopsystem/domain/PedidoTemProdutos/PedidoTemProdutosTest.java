package com.accenture.shopsystem.domain.PedidoTemProdutos;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTemProdutosTest {

    @Test
    void getId() {
        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        pedidoTemProdutos.setId("123");
        assertEquals("123", pedidoTemProdutos.getId());
    }

    @Test
    void getPedido() {
        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        Pedido pedido = new Pedido();
        pedido.setId("pedido1");
        pedidoTemProdutos.setPedido(pedido);
        assertEquals(pedido, pedidoTemProdutos.getPedido());
    }

    @Test
    void getProduto() {
        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        Produto produto = new Produto();
        produto.setId("produto1");
        pedidoTemProdutos.setProduto(produto);
        assertEquals(produto, pedidoTemProdutos.getProduto());
    }

    @Test
    void getQuantidade() {
        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        pedidoTemProdutos.setQuantidade(5);
        assertEquals(5, pedidoTemProdutos.getQuantidade());
    }

    @Test
    void getPrecoUnitario() {
        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        BigDecimal precoUnitario = new BigDecimal("99.99");
        pedidoTemProdutos.setPrecoUnitario(precoUnitario);
        assertEquals(precoUnitario, pedidoTemProdutos.getPrecoUnitario());
    }

    @Test
    void setId() {
        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        pedidoTemProdutos.setId("456");
        assertEquals("456", pedidoTemProdutos.getId());
    }

    @Test
    void setPedido() {
        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        Pedido pedido = new Pedido();
        pedido.setId("pedido2");
        pedidoTemProdutos.setPedido(pedido);
        assertEquals(pedido, pedidoTemProdutos.getPedido());
    }

    @Test
    void setProduto() {
        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        Produto produto = new Produto();
        produto.setId("produto2");
        pedidoTemProdutos.setProduto(produto);
        assertEquals(produto, pedidoTemProdutos.getProduto());
    }

    @Test
    void setQuantidade() {
        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        pedidoTemProdutos.setQuantidade(10);
        assertEquals(10, pedidoTemProdutos.getQuantidade());
    }

    @Test
    void setPrecoUnitario() {
        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        BigDecimal precoUnitario = new BigDecimal("199.99");
        pedidoTemProdutos.setPrecoUnitario(precoUnitario);
        assertEquals(precoUnitario, pedidoTemProdutos.getPrecoUnitario());
    }

    @Test
    void testEquals() {
        PedidoTemProdutos pedidoTemProdutos1 = new PedidoTemProdutos();
        pedidoTemProdutos1.setId("1");

        PedidoTemProdutos pedidoTemProdutos2 = new PedidoTemProdutos();
        pedidoTemProdutos2.setId("1");

        assertEquals(pedidoTemProdutos1, pedidoTemProdutos2);

        PedidoTemProdutos pedidoTemProdutos3 = new PedidoTemProdutos();
        pedidoTemProdutos3.setId("2");

        assertNotEquals(pedidoTemProdutos1, pedidoTemProdutos3);
    }

    @Test
    void canEqual() {
        PedidoTemProdutos pedidoTemProdutos1 = new PedidoTemProdutos();
        PedidoTemProdutos pedidoTemProdutos2 = new PedidoTemProdutos();
        assertTrue(pedidoTemProdutos1.canEqual(pedidoTemProdutos2));
    }

    @Test
    void testHashCode() {
        PedidoTemProdutos pedidoTemProdutos1 = new PedidoTemProdutos();
        pedidoTemProdutos1.setId("1");

        PedidoTemProdutos pedidoTemProdutos2 = new PedidoTemProdutos();
        pedidoTemProdutos2.setId("1");

        assertEquals(pedidoTemProdutos1.hashCode(), pedidoTemProdutos2.hashCode());

        PedidoTemProdutos pedidoTemProdutos3 = new PedidoTemProdutos();
        pedidoTemProdutos3.setId("2");

        assertNotEquals(pedidoTemProdutos1.hashCode(), pedidoTemProdutos3.hashCode());
    }

    @Test
    void testToString() {
        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        pedidoTemProdutos.setId("123");
        pedidoTemProdutos.setQuantidade(5);
        pedidoTemProdutos.setPrecoUnitario(new BigDecimal("49.99"));

        String expected = "PedidoTemProdutos{id='123', quantidade=5, precoUnitario=49.99}";
        assertEquals(expected, pedidoTemProdutos.toString());
    }
}
