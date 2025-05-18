package br.edu.reDoar.repositories;

import java.util.List;
import java.time.LocalDateTime;
import br.edu.reDoar.model.Doador;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DoadorRepository extends JpaRepository<Doador, Long> {
    boolean existsByDocumento(String documento);
    boolean existsByEmail(String email);
    Doador findByDocumentoAndTipo(String documento, String tipo);

    @Query("SELECT d FROM Doador d WHERE d.dataCadastro BETWEEN :dataInicio AND :dataFim")
    List<Doador> findByDataCadastroBetween(@Param("dataInicio") LocalDateTime dataInicio,
                                           @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT d FROM Doador d WHERE d.parceiro = :parceiro AND d.dataCadastro BETWEEN :inicio AND :fim")
    List<Doador> findByParceiroAndDataCadastroBetween(@Param("parceiro") Boolean parceiro,
                                                      @Param("inicio") LocalDateTime inicio,
                                                      @Param("fim") LocalDateTime fim);

}