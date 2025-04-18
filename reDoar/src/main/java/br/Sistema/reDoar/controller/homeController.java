package br.Sistema.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class homeController {

    @GetMapping("/")  // Mapeia a raiz do projeto
    public String index() {
        return "telainicial";
    }

    @GetMapping("/telainicial")  // Mantém a rota original
    public String telaInicial() {
        return "telainicial";
    }
}
