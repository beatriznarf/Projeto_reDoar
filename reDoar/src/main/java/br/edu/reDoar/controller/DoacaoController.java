package br.edu.reDoar.controller;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.reDoar.model.Doacao;
import br.edu.reDoar.model.Doador;
import br.edu.reDoar.repositories.DoacaoRepository;
import br.edu.reDoar.repositories.DoadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class DoacaoController {

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Autowired
    private DoadorRepository doadorRepository;



    @GetMapping("/Doacoes")
    public String telaDoacoes() {
        return "telaDoacoes";
    }

    @PostMapping("/registrarDoacao")
    public String registrarDoacao(
            @RequestParam Long doadorId,
            @RequestParam BigDecimal valor,
            @RequestParam String metodoPagamento, // Alterado de formaPagamento para metodoPagamento
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate data,
            @RequestParam(required = false) String observacoes,
            RedirectAttributes redirectAttributes) {

        try {
            Doador doador = doadorRepository.findById(doadorId)
                    .orElseThrow(() -> new RuntimeException("Doador não encontrado"));

            Doacao doacao = new Doacao();
            doacao.setDoador(doador);
            doacao.setValor(valor);
            doacao.setMetodoPagamento(metodoPagamento);
            doacao.setData(data.atStartOfDay());
            doacao.setObservacao(observacoes);

            doacaoRepository.save(doacao);

            redirectAttributes.addFlashAttribute("success", "Doação registrada com sucesso! 🎉");
            return "redirect:/Doacoes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao registrar doação: " + e.getMessage() + " ❌");
            return "redirect:/Doacoes";
        }

    }


    @GetMapping("/buscarDoacoesPorPeriodo")
    @ResponseBody
    public List<Doacao> buscarDoacoesPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        return doacaoRepository.findByDataBetween(dataInicio, dataFim);
    }

    @DeleteMapping("/excluirDoacao/{id}")
    @ResponseBody
    public Map<String, Object> excluirDoacao(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            doacaoRepository.deleteById(id);
            response.put("success", true);
            response.put("message", "Doação excluída com sucesso");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao excluir doação: " + e.getMessage());
        }
        return response;
    }
}