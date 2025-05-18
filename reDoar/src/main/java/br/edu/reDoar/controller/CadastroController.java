package br.edu.reDoar.controller;

import br.edu.reDoar.model.Usuario;
import org.springframework.stereotype.Controller;
import br.edu.reDoar.repositories.UsuarioRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class CadastroController {


    @GetMapping("/Funcionario")
    public String cadastroFuncionario() {
        return "cadastroFuncionario";
    }
    @GetMapping("/DoadorPF")
    public String cadastroDoadorPF() {
        return "cadastroDoadorPF";
    }

    @GetMapping("/DoadorPJ")
    public String cadastroDoadorPJ() {
        return "cadastroDoadorCNPJ";
    }
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/salvarFuncionario")
    public String salvarFuncionario(
            @RequestParam String nome,
            @RequestParam String cpf,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String cargo,
            RedirectAttributes redirectAttributes) {

        // Verificar se o email ou CPF já estão cadastrados
        boolean emailExiste = usuarioRepository.existsByEmail(email);
        boolean cpfExiste = usuarioRepository.existsByCpf(cpf);

        if (emailExiste || cpfExiste) {
            redirectAttributes.addFlashAttribute("errorType",
                    emailExiste && cpfExiste ? "email_cpf_existentes" :
                            emailExiste ? "email_existente" : "cpf_existente");


            redirectAttributes.addFlashAttribute("nome", nome);
            redirectAttributes.addFlashAttribute("cpf", cpf);
            redirectAttributes.addFlashAttribute("email", email);

            return "redirect:/Funcionario";
        }


        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setCargo(cargo);


        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/Funcionario";
    }
}