package br.edu.reDoar.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CadastroController {


    @GetMapping("/Doador")
    public String cadastroDoador() {
        return "cadastroDoadorPF";
    }

    @GetMapping("/Funcionario")
    public String cadastroFuncionario() {
        return "cadastroFuncionario";
    }
}