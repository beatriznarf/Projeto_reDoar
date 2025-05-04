package br.edu.reDoar.repositories;

import br.edu.reDoar.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmailAndSenha(String email, String senha);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}