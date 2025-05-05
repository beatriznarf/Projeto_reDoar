package br.edu.reDoar.controller;

import java.util.List;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.edu.reDoar.model.Doacao;
import br.edu.reDoar.model.Doador;
import br.edu.reDoar.model.Parceiro;
import br.edu.reDoar.model.Usuario;
import br.edu.reDoar.repositories.DoacaoRepository;
import br.edu.reDoar.repositories.ParceiroRepository;
import br.edu.reDoar.repositories.UsuarioRepository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

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

        @Autowired
        private ParceiroRepository parceiroRepository;


    @GetMapping("/Consultas")
        public String telaConsultas() {
              return "telaConsultas";
            }

        @PostMapping("/consultarFuncionarios")
        public String consultarFuncionarios(
                @RequestParam String dataInicio,
                @RequestParam String dataFim,
                Model model) {

            LocalDateTime inicio = parseDate(dataInicio).atStartOfDay();
            LocalDateTime fim = parseDate(dataFim).atTime(LocalTime.MAX);

            List<Usuario> funcionarios = usuarioRepository.findByDataCadastroBetween(inicio, fim);
            model.addAttribute("funcionarios", funcionarios);
            model.addAttribute("tipoConsulta", "funcionarios");

            return "telaConsultas";
        }

        @PostMapping("/consultarDoadores")
        public String consultarDoadores(
                @RequestParam String dataInicio,
                @RequestParam String dataFim,
                Model model) {

            LocalDateTime inicio = parseDate(dataInicio).atStartOfDay();
            LocalDateTime fim = parseDate(dataFim).atTime(LocalTime.MAX);

            List<Doador> doadores = doadorRepository.findByDataCadastroBetween(inicio, fim);
            model.addAttribute("doadores", doadores);
            model.addAttribute("tipoConsulta", "doadores");

            return "telaConsultas";
        }

        @PostMapping("/consultarDoacoes")
        public String consultarDoacoes(
                @RequestParam String dataInicio,
                @RequestParam String dataFim,
                Model model) {

            LocalDateTime inicio = parseDate(dataInicio).atStartOfDay();
            LocalDateTime fim = parseDate(dataFim).atTime(LocalTime.MAX);

            List<Doacao> doacoes = doacaoRepository.findByDataBetween(inicio, fim);
            model.addAttribute("doacoes", doacoes);
            model.addAttribute("tipoConsulta", "doacoes");

            return "telaConsultas";
        }

        @PostMapping("/consultarParceiros")
        public String consultarParceiros(
                @RequestParam String dataInicio,
                @RequestParam String dataFim,
                Model model) {

            LocalDateTime inicio = parseDate(dataInicio).atStartOfDay();
            LocalDateTime fim = parseDate(dataFim).atTime(LocalTime.MAX);

            List<Parceiro> parceiros = parceiroRepository.findByDataCadastroBetween(inicio, fim);
            model.addAttribute("parceiros", parceiros);
            model.addAttribute("tipoConsulta", "parceiros");

            return "telaConsultas";
        }


    private LocalDate parseDate(String dateStr) {
            // Converte de dd/MM/yyyy para LocalDate
            String[] parts = dateStr.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            return LocalDate.of(year, month, day);
        }
    }