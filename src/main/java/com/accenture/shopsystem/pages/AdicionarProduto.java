package com.accenture.shopsystem.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdicionarProduto {

    @GetMapping("/produtos/adicionar")
    public String exibirFormularioAdicionarProduto(Model model) {
        return "adicionarProduto";
    }
}
