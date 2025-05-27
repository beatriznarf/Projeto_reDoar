package br.edu.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EditarDadosController {

    @GetMapping({ "/telaEditarDados"})
    public String telaEditarDados() {
        return "telaEditarDados";
    }

    @GetMapping("/telaEditarDoacao")
    public String telaEditarDoacao() {
         return "telaEditarDoacao";
    }

}