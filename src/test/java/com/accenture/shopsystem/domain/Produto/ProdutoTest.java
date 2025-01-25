package com.accenture.shopsystem.domain.Produto;

import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    @Test
    void getDescricao() {
        Produto produto = new Produto();
        assertNull(produto.getDescricao(), "O método getDescricao() retorna null por padrão.");
    }

    @Test
    void getId() {
        Produto produto = new Produto();
        produto.setId("123");
        assertEquals("123", produto.getId());
    }

    @Test
    void getProdutoDescricao() {
        Produto produto = new Produto();
        produto.setProdutoDescricao("Produto Teste");
        assertEquals("Produto Teste", produto.getProdutoDescricao());
    }

    @Test
    void getProdutoValor() {
        Produto produto = new Produto();
        BigDecimal valor = new BigDecimal("99.99");
        produto.setProdutoValor(valor);
        assertEquals(valor, produto.getProdutoValor());
    }

    @Test
    void getProdutoDataHoraSaida() {
        Produto produto = new Produto();
        LocalDateTime dataHoraSaida = LocalDateTime.of(2023, 1, 1, 10, 0);
        produto.setProdutoDataHoraSaida(dataHoraSaida);
        assertEquals(dataHoraSaida, produto.getProdutoDataHoraSaida());
    }

    @Test
    void getQuantidadeEstoque() {
        Produto produto = new Produto();
        produto.setQuantidadeEstoque(50);
        assertEquals(50, produto.getQuantidadeEstoque());
    }

    @Test
    void getVendedor() {
        Produto produto = new Produto();
        Vendedor vendedor = new Vendedor();
        vendedor.setId("1");
        vendedor.setVendedorNome("João Silva");
        produto.setVendedor(vendedor);
        assertEquals(vendedor, produto.getVendedor());
    }

    @Test
    void setId() {
        Produto produto = new Produto();
        produto.setId("456");
        assertEquals("456", produto.getId());
    }

    @Test
    void setProdutoDescricao() {
        Produto produto = new Produto();
        produto.setProdutoDescricao("Descrição Atualizada");
        assertEquals("Descrição Atualizada", produto.getProdutoDescricao());
    }

    @Test
    void setProdutoValor() {
        Produto produto = new Produto();
        BigDecimal valor = new BigDecimal("199.99");
        produto.setProdutoValor(valor);
        assertEquals(valor, produto.getProdutoValor());
    }

    @Test
    void setProdutoDataHoraSaida() {
        Produto produto = new Produto();
        LocalDateTime dataHoraSaida = LocalDateTime.of(2023, 12, 31, 23, 59);
        produto.setProdutoDataHoraSaida(dataHoraSaida);
        assertEquals(dataHoraSaida, produto.getProdutoDataHoraSaida());
    }

    @Test
    void setQuantidadeEstoque() {
        Produto produto = new Produto();
        produto.setQuantidadeEstoque(100);
        assertEquals(100, produto.getQuantidadeEstoque());
    }

    @Test
    void setVendedor() {
        Produto produto = new Produto();
        Vendedor vendedor = new Vendedor();
        vendedor.setId("789");
        produto.setVendedor(vendedor);
        assertEquals(vendedor, produto.getVendedor());
    }

    @Test
    void testEquals() {
        Produto produto1 = new Produto();
        produto1.setId("1");

        Produto produto2 = new Produto();
        produto2.setId("1");

        assertEquals(produto1, produto2);

        Produto produto3 = new Produto();
        produto3.setId("2");

        assertNotEquals(produto1, produto3);
    }

    @Test
    void canEqual() {
        Produto produto1 = new Produto();
        Produto produto2 = new Produto();
        assertTrue(produto1.canEqual(produto2));
    }

    @Test
    void testHashCode() {
        Produto produto1 = new Produto();
        produto1.setId("1");

        Produto produto2 = new Produto();
        produto2.setId("1");

        assertEquals(produto1.hashCode(), produto2.hashCode());

        Produto produto3 = new Produto();
        produto3.setId("2");

        assertNotEquals(produto1.hashCode(), produto3.hashCode());
    }

    @Test
    void testToString() {
        Produto produto = new Produto();
        produto.setId("123");
        produto.setProdutoDescricao("Produto Exemplo");
        produto.setProdutoValor(new BigDecimal("49.99"));
        produto.setQuantidadeEstoque(30);

        String expected = "Produto{id='123', produtoDescricao='Produto Exemplo', produtoValor=49.99, quantidadeEstoque=30}";
        assertEquals(expected, produto.toString());
    }
}
