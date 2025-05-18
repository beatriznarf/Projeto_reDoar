package br.edu.reDoar.controller;

import java.time.LocalDate;
import java.math.BigDecimal;
import br.edu.reDoar.model.Doacao;
import br.edu.reDoar.model.Doador;
import br.edu.reDoar.repositories.DoacaoRepository;
import br.edu.reDoar.repositories.DoadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

            redirectAttributes.addFlashAttribute("success", "Doação registrada com sucesso!");
            return "redirect:/Doacoes";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao registrar doação: " + e.getMessage());
            return "redirect:/Doacoes";
        }
    }
}