package br.edu.reDoar.repositories;

import java.util.List;
import java.time.LocalDateTime;

import br.edu.reDoar.model.Doacao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
   
    @Query("SELECT d FROM Doacao d LEFT JOIN FETCH d.doador WHERE d.data BETWEEN :dataInicio AND :dataFim")
    List<Doacao> findByDataBetween(@Param("dataInicio") LocalDateTime dataInicio,
                                   @Param("dataFim") LocalDateTime dataFim);
}
