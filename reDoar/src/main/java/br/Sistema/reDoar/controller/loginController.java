package br.Sistema.reDoar.controller;

import br.Sistema.reDoar.service.funcionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class loginController {

    @Autowired
    private funcionarioService service;


    @GetMapping("/login")
    public String redirectToLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String senha,
                        Model model) {

        var funcionario = service.autenticar(email, senha);

        if (funcionario.isPresent()) {
            model.addAttribute("email", funcionario.get().getEmail());
            return "redirect:/telaLogado";
        } else {
            model.addAttribute("erro", "E-mail ou senha inválidos");
            return "login";
        }
    }
}