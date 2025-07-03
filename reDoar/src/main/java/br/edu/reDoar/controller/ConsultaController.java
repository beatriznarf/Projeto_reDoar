package br.edu.reDoar.controller;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.edu.reDoar.model.Doacao;
import br.edu.reDoar.model.Doador;
import br.edu.reDoar.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import br.edu.reDoar.repositories.DoacaoRepository;
import br.edu.reDoar.repositories.UsuarioRepository;
import br.edu.reDoar.repositories.DoadorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ConsultaController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DoadorRepository doadorRepository;

    @Autowired
    private DoacaoRepository doacaoRepository;

    @GetMapping("/Consultas")
    public String telaConsultas() {
        return "telaConsultas";
    }

    @PostMapping("/consultarFuncionarios")
    public String consultarFuncionarios(
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            Model model,
            HttpSession session) {

        LocalDateTime inicio = parseDate(dataInicio).atStartOfDay();
        LocalDateTime fim = parseDate(dataFim).atTime(LocalTime.MAX);

        List<Usuario> funcionarios = usuarioRepository.findByDataCadastroBetween(inicio, fim);

        int totalFuncionarios = funcionarios.size();

        session.setAttribute("funcionarios", funcionarios);
        model.addAttribute("funcionarios", funcionarios);
        model.addAttribute("tipoConsulta", "funcionarios");
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        model.addAttribute("totalRegistros", totalFuncionarios);

        return "telaConsultas";
    }

    @PostMapping("/consultarDoadores")
    public String consultarDoadores(
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            Model model,
            HttpSession session) {

        LocalDateTime inicio = parseDate(dataInicio).atStartOfDay();
        LocalDateTime fim = parseDate(dataFim).atTime(LocalTime.MAX);

        List<Doador> doadores = doadorRepository.findByDataCadastroBetween(inicio, fim);
        int totalDoadores = doadores.size();

        session.setAttribute("doadores", doadores);
        model.addAttribute("doadores", doadores);
        model.addAttribute("tipoConsulta", "doadores");
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        model.addAttribute("totalRegistros", totalDoadores);

        return "telaConsultas";
    }

    @PostMapping("/consultarDoacoes")
    public String consultarDoacoes(
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            Model model,
            HttpSession session) {

        LocalDateTime inicio = parseDate(dataInicio).atStartOfDay();
        LocalDateTime fim = parseDate(dataFim).atTime(LocalTime.MAX);

        List<Doacao> doacoes = doacaoRepository.findByDataBetween(inicio, fim);
        int totalDoacoes = doacoes.size();

        BigDecimal totalValorDoacoes = doacoes.stream()
                .map(Doacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        session.setAttribute("doacoes", doacoes);
        session.setAttribute("totalDoacoes", totalValorDoacoes);
        model.addAttribute("doacoes", doacoes);
        model.addAttribute("totalDoacoes", totalValorDoacoes);
        model.addAttribute("tipoConsulta", "doacoes");
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        model.addAttribute("totalRegistros", totalDoacoes);

        return "telaConsultas";
    }

    @PostMapping("/consultarParceiros")
    public String consultarParceiros(
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            Model model,
            HttpSession session) {

        LocalDateTime inicio = parseDate(dataInicio).atStartOfDay();
        LocalDateTime fim = parseDate(dataFim).atTime(LocalTime.MAX);

        List<Doador> parceiros = doadorRepository.findByParceiroAndDataCadastroBetween(true, inicio, fim);

        int totalParceiros = parceiros.size();

        session.setAttribute("parceiros", parceiros);
        model.addAttribute("parceiros", parceiros);
        model.addAttribute("tipoConsulta", "parceiros");
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        model.addAttribute("totalRegistros", totalParceiros);

        return "telaConsultas";
    }

    private LocalDate parseDate(String dateStr) {
        String[] parts = dateStr.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        return LocalDate.of(year, month, day);
    }
}
