package br.Sistema.reDoar.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class doadorController {

    @GetMapping("/doadorCadastro")
    public String doador() {
        return "doadorCadastro";
    }
}

