package com.accenture.shopsystem.pages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CadastroTest {

    @Test
    void exibirPaginaCadastro() {
        Cadastro cadastro = new Cadastro();

        String viewName = cadastro.exibirPaginaCadastro();

        assertEquals("cadastro", viewName);
    }
}
