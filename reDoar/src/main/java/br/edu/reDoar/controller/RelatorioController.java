package br.edu.reDoar.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.*;
import br.edu.reDoar.model.Doacao;
import br.edu.reDoar.model.Doador;
import jakarta.servlet.http.HttpSession;
import br.edu.reDoar.service.RelatorioService;
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
            @RequestParam(required = false) String filtroTipo,
            @RequestParam(required = false) String filtroPagamento,
            HttpSession session) {

        List<?> dados = null;

        switch(tipo) {
            case "doadores":
                List<Doador> todosDoadores = (List<Doador>) session.getAttribute("doadores");
                if (filtroTipo != null && !filtroTipo.isEmpty()) {
                    dados = todosDoadores.stream()
                            .filter(d -> filtroTipo.equals(d.getTipo()))
                            .collect(Collectors.toList());
                } else {
                    dados = todosDoadores;
                }
                break;

            case "doacoes":
                List<Doacao> todasDoacoes = (List<Doacao>) session.getAttribute("doacoes");
                if (filtroPagamento != null && !filtroPagamento.isEmpty()) {
                    dados = todasDoacoes.stream()
                            .filter(d -> filtroPagamento.equalsIgnoreCase(d.getMetodoPagamento()))
                            .collect(Collectors.toList());
                } else {
                    dados = todasDoacoes;
                }
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
                    ContentDisposition.builder("attachment")
                            .filename("relatorio-" + tipo + ".pdf")
                            .build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}