package com.accenture.shopsystem.domain.Pedido;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    void getId() {
        Pedido pedido = new Pedido();
        pedido.setId("123");
        assertEquals("123", pedido.getId());
    }

    @Test
    void getPedidoDescricao() {
        Pedido pedido = new Pedido();
        pedido.setPedidoDescricao("Descrição do Pedido");
        assertEquals("Descrição do Pedido", pedido.getPedidoDescricao());
    }

    @Test
    void getPedidoValor() {
        Pedido pedido = new Pedido();
        BigDecimal valor = new BigDecimal("299.99");
        pedido.setPedidoValor(valor);
        assertEquals(valor, pedido.getPedidoValor());
    }

    @Test
    void getPedidoDataHora() {
        Pedido pedido = new Pedido();
        LocalDateTime dataHora = LocalDateTime.of(2025, 1, 1, 12, 0);
        pedido.setPedidoDataHora(dataHora);
        assertEquals(dataHora, pedido.getPedidoDataHora());
    }

    @Test
    void getPedidoQuantidade() {
        Pedido pedido = new Pedido();
        pedido.setPedidoQuantidade(10);
        assertEquals(10, pedido.getPedidoQuantidade());
    }

    @Test
    void getVendedor() {
        Pedido pedido = new Pedido();
        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedor1");
        pedido.setVendedor(vendedor);
        assertEquals(vendedor, pedido.getVendedor());
    }

    @Test
    void getProdutos() {
        Pedido pedido = new Pedido();
        List<PedidoTemProdutos> produtos = new ArrayList<>();
        pedido.setProdutos(produtos);
        assertSame(produtos, pedido.getProdutos());
    }

    @Test
    void setId() {
        Pedido pedido = new Pedido();
        pedido.setId("456");
        assertEquals("456", pedido.getId());
    }

    @Test
    void setPedidoDescricao() {
        Pedido pedido = new Pedido();
        pedido.setPedidoDescricao("Nova Descrição");
        assertEquals("Nova Descrição", pedido.getPedidoDescricao());
    }

    @Test
    void setPedidoValor() {
        Pedido pedido = new Pedido();
        BigDecimal valor = new BigDecimal("399.99");
        pedido.setPedidoValor(valor);
        assertEquals(valor, pedido.getPedidoValor());
    }

    @Test
    void setPedidoDataHora() {
        Pedido pedido = new Pedido();
        LocalDateTime dataHora = LocalDateTime.of(2025, 1, 2, 14, 0);
        pedido.setPedidoDataHora(dataHora);
        assertEquals(dataHora, pedido.getPedidoDataHora());
    }

    @Test
    void setPedidoQuantidade() {
        Pedido pedido = new Pedido();
        pedido.setPedidoQuantidade(5);
        assertEquals(5, pedido.getPedidoQuantidade());
    }

    @Test
    void setVendedor() {
        Pedido pedido = new Pedido();
        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedor2");
        pedido.setVendedor(vendedor);
        assertEquals(vendedor, pedido.getVendedor());
    }

    @Test
    void setProdutos() {
        Pedido pedido = new Pedido();
        List<PedidoTemProdutos> produtos = new ArrayList<>();
        pedido.setProdutos(produtos);
        assertSame(produtos, pedido.getProdutos());
    }

    @Test
    void testEquals() {
        Pedido pedido1 = new Pedido();
        pedido1.setId("1");

        Pedido pedido2 = new Pedido();
        pedido2.setId("1");

        assertEquals(pedido1, pedido2);

        Pedido pedido3 = new Pedido();
        pedido3.setId("2");

        assertNotEquals(pedido1, pedido3);
    }

    @Test
    void canEqual() {
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        assertTrue(pedido1.canEqual(pedido2));
    }

    @Test
    void testHashCode() {
        Pedido pedido1 = new Pedido();
        pedido1.setId("1");

        Pedido pedido2 = new Pedido();
        pedido2.setId("1");

        assertEquals(pedido1.hashCode(), pedido2.hashCode());

        Pedido pedido3 = new Pedido();
        pedido3.setId("2");

        assertNotEquals(pedido1.hashCode(), pedido3.hashCode());
    }

    @Test
    void testToString() {
        Pedido pedido = new Pedido();
        pedido.setId("123");
        pedido.setPedidoDescricao("Pedido de Exemplo");
        pedido.setPedidoValor(new BigDecimal("149.99"));
        pedido.setPedidoQuantidade(3);

        String expected = "Pedido{id='123', pedidoDescricao='Pedido de Exemplo', pedidoValor=149.99, pedidoQuantidade=3}";
        assertEquals(expected, pedido.toString());
    }
}
