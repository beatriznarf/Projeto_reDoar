package br.edu.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/SaibaMais")
    public String telaSaibaMais() {
        return "telaSaibaMais";
    }

    @GetMapping("/SobreAssociacao")
    public String telaSobreAssociacao() {
        return "telaSobreAssociacao";
    }

    @GetMapping("/TransformeVidas")
    public String telaTransformeVidas() {
        return "telaTransformeVidas";
    }

    @GetMapping("/Galeria")
    public String telaGaleria() {
        return "telaGaleria";
    }

}

