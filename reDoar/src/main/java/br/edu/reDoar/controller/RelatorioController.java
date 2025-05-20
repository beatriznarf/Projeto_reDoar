package br.edu.reDoar.controller;

import java.util.List;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.MediaType;
import com.itextpdf.text.DocumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import br.edu.reDoar.service.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;


import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;



    @PostMapping("/gerar/{tipo}")
    public ResponseEntity<byte[]> gerarRelatorio(
            @PathVariable String tipo,
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            @RequestParam(required = false) String filtro) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime inicio = LocalDate.parse(dataInicio, formatter).atStartOfDay();
            LocalDateTime fim = LocalDate.parse(dataFim, formatter).atTime(LocalTime.MAX);

            List<?> dados = relatorioService.buscarDadosRelatorio(tipo, inicio, fim);

            if (dados == null || dados.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            byte[] pdf = relatorioService.gerarRelatorioPDF(tipo, dados, dataInicio, dataFim);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-" + tipo + ".pdf");

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (DocumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




}