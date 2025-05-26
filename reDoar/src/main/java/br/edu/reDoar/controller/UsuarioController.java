package br.edu.reDoar.controller;

import java.util.Map;
import java.util.List;
import br.edu.reDoar.model.Usuario;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import br.edu.reDoar.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/buscarTodosUsuarios")
    @ResponseBody
    public List<Usuario> buscarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/buscarUsuario/{id}")
    @ResponseBody
    public Usuario buscarUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @PostMapping("/atualizarUsuario")
    @ResponseBody
    public Map<String, String> atualizarUsuario(@RequestBody Usuario usuarioAtualizado) {
        try {
            Usuario usuarioExistente = usuarioRepository.findById(usuarioAtualizado.getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Atualiza apenas os campos que foram enviados
            if (usuarioAtualizado.getNome() != null) {
                usuarioExistente.setNome(usuarioAtualizado.getNome());
            }
            if (usuarioAtualizado.getEmail() != null) {
                usuarioExistente.setEmail(usuarioAtualizado.getEmail());
            }
            if (usuarioAtualizado.getCpf() != null) {
                usuarioExistente.setCpf(usuarioAtualizado.getCpf());
            }
            if (usuarioAtualizado.getCargo() != null) {
                usuarioExistente.setCargo(usuarioAtualizado.getCargo());
            }
            if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
                usuarioExistente.setSenha(usuarioAtualizado.getSenha());
            }

            usuarioRepository.save(usuarioExistente);
            return Map.of("message", "Usuário atualizado com sucesso");
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
}
