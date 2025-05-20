package br.edu.reDoar.repositories;

import java.util.List;
import java.time.LocalDateTime;

import br.edu.reDoar.model.Parceiro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParceiroRepository extends JpaRepository<Parceiro, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT p FROM Parceiro p WHERE p.dataCadastro BETWEEN :dataInicio AND :dataFim")
    List<Parceiro> findByDataCadastroBetween(@Param("dataInicio") LocalDateTime dataInicio,
                                             @Param("dataFim") LocalDateTime dataFim);
}
