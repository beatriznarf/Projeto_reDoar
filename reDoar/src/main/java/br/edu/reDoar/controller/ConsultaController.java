package br.edu.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConsultaController {

    @GetMapping("/Consultas")
    public String telaConsultas() {
        return "telaConsultas";
    }
}