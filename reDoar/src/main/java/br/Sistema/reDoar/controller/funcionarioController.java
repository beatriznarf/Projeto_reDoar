package br.Sistema.reDoar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class funcionarioController {

        @GetMapping("/funcionarioCadastro")
        public String funcionario() {
            return "funcionarioCadastro";
        }
    }


