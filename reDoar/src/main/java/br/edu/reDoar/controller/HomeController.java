package br.edu.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/telainicial"})
    public String telaInicial() {
        return "telainicial";
    }

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

    @GetMapping("/Projetos")
    public String telaProjetos() {
        return "telaProjetos";
    }

    @GetMapping("/Contatos")
    public String telaContatos() {
        return "telaContatos";
    }

    @GetMapping("/Parcerias")
    public String telaParcerias() {
        return "telaParcerias";
    }

    @GetMapping("/Galeria")
    public String telaGaleria() {
        return "telaGaleria";
    }

}

