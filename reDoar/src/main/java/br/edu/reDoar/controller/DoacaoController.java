package br.edu.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoacaoController {

    @GetMapping("/Doacoes")
    public String telaDoacoes() {
        return "telaDoacoes";
    }
}