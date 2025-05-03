package br.edu.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CadastroController {

    @GetMapping("/cadastroFuncionario")
    public String cadastroFuncionario() {
        return "cadastroFuncionario";
    }

    @GetMapping("/cadastroDoador")
    public String cadastroDoador() {
        return "cadastroDoador";
    }
}
