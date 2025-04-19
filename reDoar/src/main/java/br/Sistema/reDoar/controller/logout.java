package br.Sistema.reDoar.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class logout{


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Encerra a sessão do usuário
        return "redirect:/telainicial"; // Redireciona para a tela inicial
    }

}
