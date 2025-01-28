package com.accenture.shopsystem.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleProdutoNaoEncontrado() {
        ShopSystemExceptions.ProdutoNaoEncontradoException exception =
                new ShopSystemExceptions.ProdutoNaoEncontradoException("Produto não encontrado");

        ResponseEntity<String> response = exceptionHandler.handleProdutoNaoEncontrado(exception);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Produto não encontrado", response.getBody());
    }

    @Test
    void handleEstoqueInsuficiente() {
        ShopSystemExceptions.EstoqueInsuficienteException exception =
                new ShopSystemExceptions.EstoqueInsuficienteException("Estoque insuficiente");

        ResponseEntity<String> response = exceptionHandler.handleEstoqueInsuficiente(exception);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Estoque insuficiente", response.getBody());
    }

    @Test
    void handleVendedorNaoEncontrado() {
        ShopSystemExceptions.VendedorNaoEncontradoException exception =
                new ShopSystemExceptions.VendedorNaoEncontradoException("Vendedor não encontrado");

        ResponseEntity<String> response = exceptionHandler.handleVendedorNaoEncontrado(exception);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Vendedor não encontrado", response.getBody());
    }

    @Test
    void handlePedidoNaoEncontrado() {
        ShopSystemExceptions.PedidoNaoEncontradoException exception =
                new ShopSystemExceptions.PedidoNaoEncontradoException("Pedido não encontrado");

        ResponseEntity<String> response = exceptionHandler.handlePedidoNaoEncontrado(exception);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Pedido não encontrado", response.getBody());
    }

    @Test
    void handleAcaoInvalida() {
        ShopSystemExceptions.AcaoInvalidaException exception =
                new ShopSystemExceptions.AcaoInvalidaException("Ação inválida");

        ResponseEntity<String> response = exceptionHandler.handleAcaoInvalida(exception);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Ação inválida", response.getBody());
    }

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Argumento inválido");

        ResponseEntity<String> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Argumento inválido: Argumento inválido", response.getBody());
    }

    @Test
    void handleRuntimeException() {
        RuntimeException exception = new RuntimeException("Erro inesperado");

        ResponseEntity<String> response = exceptionHandler.handleRuntimeException(exception);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro inesperado: Erro inesperado", response.getBody());
    }

    @Test
    void handleGenericException() {
        Exception exception = new Exception("Erro interno");

        ResponseEntity<String> response = exceptionHandler.handleGenericException(exception);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno: Erro interno", response.getBody());
    }
}
