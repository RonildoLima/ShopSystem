package com.accenture.shopsystem.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShopSystemExceptionsTest {

    @Test
    void testProdutoNaoEncontradoException() {
        ShopSystemExceptions.ProdutoNaoEncontradoException exception =
                new ShopSystemExceptions.ProdutoNaoEncontradoException("Produto não encontrado");

        assertEquals("Produto não encontrado", exception.getMessage());
        assertThrows(ShopSystemExceptions.ProdutoNaoEncontradoException.class, () -> {
            throw exception;
        });
    }

    @Test
    void testEstoqueInsuficienteException() {
        ShopSystemExceptions.EstoqueInsuficienteException exception =
                new ShopSystemExceptions.EstoqueInsuficienteException("Estoque insuficiente");

        assertEquals("Estoque insuficiente", exception.getMessage());
        assertThrows(ShopSystemExceptions.EstoqueInsuficienteException.class, () -> {
            throw exception;
        });
    }

    @Test
    void testVendedorNaoEncontradoException() {
        ShopSystemExceptions.VendedorNaoEncontradoException exception =
                new ShopSystemExceptions.VendedorNaoEncontradoException("Vendedor não encontrado");

        assertEquals("Vendedor não encontrado", exception.getMessage());
        assertThrows(ShopSystemExceptions.VendedorNaoEncontradoException.class, () -> {
            throw exception;
        });
    }

    @Test
    void testPedidoNaoEncontradoException() {
        ShopSystemExceptions.PedidoNaoEncontradoException exception =
                new ShopSystemExceptions.PedidoNaoEncontradoException("Pedido não encontrado");

        assertEquals("Pedido não encontrado", exception.getMessage());
        assertThrows(ShopSystemExceptions.PedidoNaoEncontradoException.class, () -> {
            throw exception;
        });
    }

    @Test
    void testAcaoInvalidaException() {
        ShopSystemExceptions.AcaoInvalidaException exception =
                new ShopSystemExceptions.AcaoInvalidaException("Ação inválida");

        assertEquals("Ação inválida", exception.getMessage());
        assertThrows(ShopSystemExceptions.AcaoInvalidaException.class, () -> {
            throw exception;
        });
    }
}
