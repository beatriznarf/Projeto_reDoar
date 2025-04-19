package br.Sistema.reDoar.repository;

import java.util.Optional;

import br.Sistema.reDoar.Model.funcionarioModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface funcionarioRepository extends JpaRepository<funcionarioModel, Long> {
    Optional<funcionarioModel> findByEmail(String email);
}