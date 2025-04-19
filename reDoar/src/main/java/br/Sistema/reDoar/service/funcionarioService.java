package br.Sistema.reDoar.service;

import br.Sistema.reDoar.Model.funcionarioModel;
import br.Sistema.reDoar.repository.funcionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class funcionarioService {

    @Autowired
    private funcionarioRepository repository;

    public Optional<funcionarioModel> autenticar(String email, String senha) {
        Optional<funcionarioModel> funcionario = repository.findByEmail(email);
        if (funcionario.isPresent() && funcionario.get().getSenha().equals(senha)) {
            return funcionario;
        }
        return Optional.empty();
    }
}

