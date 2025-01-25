package com.accenture.shopsystem.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Cadastro {

    @GetMapping("/user/cadastrar")
    public String exibirPaginaCadastro() {
        return "cadastro";
    }
}
