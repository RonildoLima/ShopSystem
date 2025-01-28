package com.accenture.shopsystem.pages;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class AdicionarProdutoTest {

    @Test
    void exibirFormularioAdicionarProduto() {
        Model model = mock(Model.class);
        AdicionarProduto adicionarProduto = new AdicionarProduto();

        String viewName = adicionarProduto.exibirFormularioAdicionarProduto(model);

        assertEquals("adicionarProduto", viewName);
    }
}
