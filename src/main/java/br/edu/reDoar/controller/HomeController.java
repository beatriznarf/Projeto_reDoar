package br.edu.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/telainicial"})
    public String telaInicial() {
        return "telainicial"; // Retorna o template telainicial.html
    }
}