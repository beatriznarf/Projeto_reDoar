package br.edu.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String Login() {
        return "telaLogin"; // procura por templates/telaLogin.html
    }
}