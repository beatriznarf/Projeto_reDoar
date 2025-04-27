package br.Sistema.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/telainicial"})  // Mapeia ambas as URLs com um único método
    public String telaInicial() {
        return "telainicial"; // Retorna o template telainicial.html
    }
}