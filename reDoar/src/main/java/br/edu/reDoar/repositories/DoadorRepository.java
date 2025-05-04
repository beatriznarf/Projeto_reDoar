package br.edu.reDoar.repositories;

import br.edu.reDoar.model.Doador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoadorRepository extends JpaRepository<Doador, Long> {
    boolean existsByDocumento(String documento);
    boolean existsByEmail(String email);
    Doador findByDocumentoAndTipo(String documento, String tipo);
}