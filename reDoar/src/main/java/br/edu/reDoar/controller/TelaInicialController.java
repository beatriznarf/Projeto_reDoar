package br.edu.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TelaInicialController {

    @GetMapping({"/", "/telainicial"})
    public String telaInicial() {
        return "telaInicial";
    }

}
