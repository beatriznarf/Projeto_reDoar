package br.Sistema.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LogadoController {

    @GetMapping("/telaLogado")
    public String redirectToLog() {
        return "telaLogado";
    }
}

