package br.edu.reDoar.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
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
            HttpSession session) {

        List<?> dados = null;

         switch(tipo.toLowerCase()) {
            case "doadores":
                dados = (List<?>) session.getAttribute("doadores");
                break;
            case "doacoes":
                dados = (List<?>) session.getAttribute("doacoes");
                break;
            case "funcionarios":
                dados = (List<?>) session.getAttribute("funcionarios");
                break;
            case "parceiros":
                dados = (List<?>) session.getAttribute("parceiros");
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        if (dados == null || dados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        try {

            byte[] pdf = relatorioService.gerarRelatorioPDF(tipo, dados, dataInicio, dataFim);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    org.springframework.http.ContentDisposition.builder("attachment")
                            .filename("relatorio-" + tipo + ".pdf")
                            .build());


            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

     private boolean validarDatas(String dataInicio, String dataFim) {
        if (dataInicio == null || dataFim == null ||
                dataInicio.trim().isEmpty() || dataFim.trim().isEmpty()) {
            return false;
        }

         return true;
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao gerar relatório: " + e.getMessage());
    }
}
