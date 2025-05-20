package br.edu.reDoar.controller;


import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

import br.edu.reDoar.model.Doador;
import org.springframework.stereotype.Controller;
import br.edu.reDoar.repositories.DoadorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class DoadorController {

    @Autowired
    private DoadorRepository doadorRepository;


    @PostMapping("/salvarDoadorPF")
    public String salvarDoadorPF(
            @RequestParam String nomeCompleto,
            @RequestParam String cpf,
            @RequestParam String email,
            @RequestParam String telefone,
            @RequestParam String cep,
            @RequestParam String endereco,
            @RequestParam String cidade,
            @RequestParam String estado,
            RedirectAttributes redirectAttributes) {

        // Verificar se CPF ou email já existem
        if (doadorRepository.existsByDocumento(cpf)) {
            redirectAttributes.addFlashAttribute("error", "CPF já cadastrado");
            return "redirect:/DoadorPF";
        }

        if (doadorRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email já cadastrado");
            return "redirect:/DoadorPF";
        }

        Doador doador = new Doador();
        doador.setTipo("PF");
        doador.setNomeCompleto(nomeCompleto);
        doador.setDocumento(cpf);
        doador.setEmail(email);
        doador.setTelefone(telefone);
        doador.setCep(cep);
        doador.setEndereco(endereco);
        doador.setCidade(cidade);
        doador.setEstado(estado);

        doadorRepository.save(doador);

        redirectAttributes.addFlashAttribute("success", "Doador Pessoa Física cadastrado com sucesso!");
        return "redirect:/DoadorPF";
    }

    // Metodo para salvar Pessoa Jurídica
    @PostMapping("/salvarDoadorPJ")
    public String salvarDoadorPJ(
            @RequestParam String razaoSocial,
            @RequestParam String cnpj,
            @RequestParam String email,
            @RequestParam String telefone,
            @RequestParam String responsavel,
            @RequestParam(required = false) Boolean parceiro,
            @RequestParam String cep,
            @RequestParam String endereco,
            @RequestParam String cidade,
            @RequestParam String estado,
            RedirectAttributes redirectAttributes) {

        // Verificar se CNPJ ou email já existem
        if (doadorRepository.existsByDocumento(cnpj)) {
            redirectAttributes.addFlashAttribute("error", "CNPJ já cadastrado");
            return "redirect:/DoadorPJ";
        }

        if (doadorRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email já cadastrado");
            return "redirect:/DoadorPJ";
        }

        Doador doador = new Doador();
        doador.setTipo("PJ");
        doador.setRazaoSocial(razaoSocial);
        doador.setDocumento(cnpj);
        doador.setEmail(email);
        doador.setTelefone(telefone);
        doador.setResponsavel(responsavel);
        doador.setParceiro(parceiro != null && parceiro);
        doador.setCep(cep);
        doador.setEndereco(endereco);
        doador.setCidade(cidade);
        doador.setEstado(estado);

        doadorRepository.save(doador);

        redirectAttributes.addFlashAttribute("success", "Doador Pessoa Jurídica cadastrado com sucesso!");
        return "redirect:/DoadorPJ";
    }


    @GetMapping("/listarDoadores")
    @ResponseBody
    public List<Map<String, Object>> listarDoadores() {
        List<Doador> doadores = doadorRepository.findAll();

        return doadores.stream().map(doador -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", doador.getId());
            map.put("nome", doador.getTipo().equals("PF") ?
                    doador.getNomeCompleto() :
                    doador.getRazaoSocial());
            map.put("tipo", doador.getTipo());
            map.put("documento", doador.getDocumento());
            return map;
        }).collect(Collectors.toList());
    }
}