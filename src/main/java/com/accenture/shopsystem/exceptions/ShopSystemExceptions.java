package com.accenture.shopsystem.exceptions;

public class ShopSystemExceptions {

    // Exceção para Produto Não Encontrado
    public static class ProdutoNaoEncontradoException extends RuntimeException {
        public ProdutoNaoEncontradoException(String message) {
            super(message);
        }
    }

    // Exceção para Estoque Insuficiente
    public static class EstoqueInsuficienteException extends RuntimeException {
        public EstoqueInsuficienteException(String message) {
            super(message);
        }
    }

    // Exceção para Vendedor Não Encontrado
    public static class VendedorNaoEncontradoException extends RuntimeException {
        public VendedorNaoEncontradoException(String message) {
            super(message);
        }
    }

    // Exceção para Pedido Não Encontrado
    public static class PedidoNaoEncontradoException extends RuntimeException {
        public PedidoNaoEncontradoException(String message) {
            super(message);
        }
    }

    // Exceção para Ação Inválida
    public static class AcaoInvalidaException extends RuntimeException {
        public AcaoInvalidaException(String message) {
            super(message);
        }
    }
}
