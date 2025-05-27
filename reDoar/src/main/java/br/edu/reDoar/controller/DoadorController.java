package br.edu.reDoar.controller;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

import br.edu.reDoar.model.Doador;
import br.edu.reDoar.model.Parceiro;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import br.edu.reDoar.repositories.DoadorRepository;
import br.edu.reDoar.repositories.ParceiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DoadorController {

    @Autowired
    private DoadorRepository doadorRepository;

    @Autowired
    private ParceiroRepository parceiroRepository;

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

        if (doadorRepository.existsByDocumento(cnpj)) {
            redirectAttributes.addFlashAttribute("error", "CNPJ já cadastrado");
            return "redirect:/DoadorPJ";
        }

        if (doadorRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email já cadastrado");
            return "redirect:/DoadorPJ";
        }

        if (parceiro != null && parceiro && parceiroRepository.existsByCnpj(cnpj)) {
            redirectAttributes.addFlashAttribute("error", "CNPJ já cadastrado como parceiro");
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

        if (parceiro != null && parceiro) {
            Parceiro novoParceiro = new Parceiro();
            novoParceiro.setCnpj(cnpj);
            novoParceiro.setEmail(email);
            novoParceiro.setRazaoSocial(razaoSocial);
            novoParceiro.setResponsavel(responsavel);
            novoParceiro.setTelefone(telefone);
            novoParceiro.setCep(cep);
            novoParceiro.setEndereco(endereco);
            novoParceiro.setCidade(cidade);
            novoParceiro.setEstado(estado);

            try {
                parceiroRepository.save(novoParceiro);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("warning",
                        "Doador cadastrado, mas houve um erro ao registrar como parceiro: " + e.getMessage());
                return "redirect:/DoadorPJ";
            }
        }

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




    @GetMapping("/listarDoadoresPF")
    @ResponseBody
    public List<Map<String, Object>> listarDoadoresPF() {
        return doadorRepository.findAll().stream()
                .filter(doador -> "PF".equals(doador.getTipo()))
                .map(doador -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", doador.getId());
                    map.put("nome", doador.getNomeCompleto());
                    map.put("documento", doador.getDocumento());
                    return map;
                }).collect(Collectors.toList());
    }

    @GetMapping("/listarDoadoresPJ")
    @ResponseBody
    public List<Map<String, Object>> listarDoadoresPJ() {
        return doadorRepository.findAll().stream()
                .filter(doador -> "PJ".equals(doador.getTipo()))
                .map(doador -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", doador.getId());
                    map.put("nome", doador.getRazaoSocial());
                    map.put("documento", doador.getDocumento());
                    return map;
                }).collect(Collectors.toList());
    }

    @GetMapping("/buscarDoador/{id}")
    @ResponseBody
    public Doador buscarDoador(@PathVariable Long id) {
        return doadorRepository.findById(id).orElse(null);
    }





    @PostMapping("/atualizarDoadorPF")
    @ResponseBody
    public Map<String, String> atualizarDoadorPF(
            @RequestParam Long id,
            @RequestParam(required = false) String nomeCompleto,
            @RequestParam(required = false) String documento,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String cep,
            @RequestParam(required = false) String endereco,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado) {

        Map<String, String> response = new HashMap<>();
        try {
            Doador doador = doadorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Doador não encontrado"));

            if (!doador.getTipo().equals("PF")) {
                throw new RuntimeException("ID não corresponde a um doador PF");
            }

            // Verifica se o novo documento já existe (se foi alterado)
            if (documento != null && !documento.equals(doador.getDocumento()) &&
                    doadorRepository.existsByDocumento(documento)) {
                response.put("status", "error");
                response.put("message", "CPF já cadastrado");
                return response;
            }

            // Verifica se o novo email já existe (se foi alterado)
            if (email != null && !email.equals(doador.getEmail()) &&
                    doadorRepository.existsByEmail(email)) {
                response.put("status", "error");
                response.put("message", "Email já cadastrado");
                return response;
            }

            if (nomeCompleto != null && !nomeCompleto.isEmpty()) doador.setNomeCompleto(nomeCompleto);
            if (documento != null && !documento.isEmpty()) doador.setDocumento(documento);
            if (email != null && !email.isEmpty()) doador.setEmail(email);
            if (telefone != null && !telefone.isEmpty()) doador.setTelefone(telefone);
            if (cep != null && !cep.isEmpty()) doador.setCep(cep);
            if (endereco != null && !endereco.isEmpty()) doador.setEndereco(endereco);
            if (cidade != null && !cidade.isEmpty()) doador.setCidade(cidade);
            if (estado != null && !estado.isEmpty()) doador.setEstado(estado);

            doadorRepository.save(doador);
            response.put("status", "success");
            response.put("message", "Dados atualizados com sucesso!");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/atualizarDoadorPJ")
    @ResponseBody
    public Map<String, String> atualizarDoadorPJ(
            @RequestParam Long id,
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) String documento,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String responsavel,
            @RequestParam(required = false) Boolean parceiro,
            @RequestParam(required = false) String cep,
            @RequestParam(required = false) String endereco,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado) {

        Map<String, String> response = new HashMap<>();
        try {
            Doador doador = doadorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Doador não encontrado"));

            if (!doador.getTipo().equals("PJ")) {
                throw new RuntimeException("ID não corresponde a um doador PJ");
            }

            // Verifica se o novo documento já existe (se foi alterado)
            if (documento != null && !documento.equals(doador.getDocumento()) &&
                    doadorRepository.existsByDocumento(documento)) {
                response.put("status", "error");
                response.put("message", "CNPJ já cadastrado");
                return response;
            }

            // Verifica se o novo email já existe (se foi alterado)
            if (email != null && !email.equals(doador.getEmail()) &&
                    doadorRepository.existsByEmail(email)) {
                response.put("status", "error");
                response.put("message", "Email já cadastrado");
                return response;
            }

            if (razaoSocial != null && !razaoSocial.isEmpty()) {
                doador.setRazaoSocial(razaoSocial);
            }

            if (razaoSocial != null && !razaoSocial.isEmpty()) doador.setRazaoSocial(razaoSocial);
            if (documento != null && !documento.isEmpty()) doador.setDocumento(documento);
            if (email != null && !email.isEmpty()) doador.setEmail(email);
            if (telefone != null && !telefone.isEmpty()) doador.setTelefone(telefone);
            if (responsavel != null && !responsavel.isEmpty()) doador.setResponsavel(responsavel);
            if (parceiro != null) doador.setParceiro(parceiro);
            if (cep != null && !cep.isEmpty()) doador.setCep(cep);
            if (endereco != null && !endereco.isEmpty()) doador.setEndereco(endereco);
            if (cidade != null && !cidade.isEmpty()) doador.setCidade(cidade);
            if (estado != null && !estado.isEmpty()) doador.setEstado(estado);

            doadorRepository.save(doador);
            response.put("status", "success");
            response.put("message", "Dados atualizados com sucesso!");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }
}
