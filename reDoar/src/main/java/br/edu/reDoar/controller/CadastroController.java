package br.edu.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CadastroController {


    @GetMapping("/Doador")
    public String cadastroDoador() {
        return "cadastroDoador"; // Retorna apenas o HTML estático
    }

    @GetMapping("/Funcionario")
    public String cadastroFuncionario() {
        return "cadastroFuncionario";
    }
}