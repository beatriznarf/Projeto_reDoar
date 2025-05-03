package br.edu.reDoar.controller;

import br.edu.reDoar.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.edu.reDoar.repositories.UsuarioRepository;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String login() {
        return "telaLogin";
    }

    @PostMapping("/login")
    public String autenticar(@RequestParam String email,
                             @RequestParam String senha) {

        // Busca o usuário no banco de dados
        Usuario usuario = usuarioRepository.findByEmailAndSenha(email, senha);

        if (usuario != null) {
            // Credenciais válidas - redireciona para tela autenticada
            return "redirect:/telaAutenticado";
        } else {
            // Credenciais inválidas - volta para tela de login com erro
            return "redirect:/login?error";
        }
    }

    @GetMapping("/telaAutenticado")
    public String telaAutenticado() {
        return "telaAutenticado";
    }
}