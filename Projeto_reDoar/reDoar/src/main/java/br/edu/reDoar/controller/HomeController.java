package br.edu.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/telainicial"})
    public String telaInicial() { return "telainicial"; // Retorna o template telainicial.html
    }

    @GetMapping("/SaibaMais")
    public String telaSaibaMais() { return "telaSaibaMais";
    }

    @GetMapping("/SobreAssociacao")
    public String telaSobreAssociacao() { return "telaSobreAssociacao";
    }

    @GetMapping("/TransformeVidas")
    public String telaTransformeVidas() { return "telaTransformeVidas";
    }

}

