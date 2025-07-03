package br.edu.reDoar.service;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.*;
import br.edu.reDoar.model.Doador;
import br.edu.reDoar.model.Doacao;
import br.edu.reDoar.model.Usuario;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import br.edu.reDoar.repositories.DoadorRepository;
import br.edu.reDoar.repositories.DoacaoRepository;
import br.edu.reDoar.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class RelatorioService {


    @Autowired
    private DoadorRepository doadorRepository;

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font CELL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final BaseColor HEADER_COLOR = new BaseColor(150, 96, 51);

    public List<?> buscarDadosRelatorio(String tipo, LocalDateTime dataInicio, LocalDateTime dataFim) {
        switch (tipo.toLowerCase()) {
            case "doadores":
                return doadorRepository.findByDataCadastroBetween(dataInicio, dataFim);
            case "doacoes":
                return doacaoRepository.findByDataBetween(dataInicio, dataFim);
            case "funcionarios":
                return usuarioRepository.findByDataCadastroBetween(dataInicio, dataFim);
            case "parceiros":
                return doadorRepository.findByParceiroAndDataCadastroBetween(true, dataInicio, dataFim);
            default:
                throw new IllegalArgumentException("Tipo de relatório não suportado: " + tipo);
        }
    }

    public byte[] gerarRelatorioPDF(String tipoRelatorio, List<?> dados, String dataInicio, String dataFim) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 60, 36);
        PdfWriter.getInstance(document, baos);

        document.open();
        adicionarCabecalho(document, tipoRelatorio, dataInicio, dataFim);

        Paragraph contagem = new Paragraph("Total de registros: " + dados.size(), CELL_FONT);
        contagem.setAlignment(Element.ALIGN_RIGHT);
        contagem.setSpacingAfter(10);
        document.add(contagem);

        PdfPTable table = criarTabelaBaseadoNoTipo(tipoRelatorio, dados);
        document.add(table);

        adicionarRodape(document);
        document.close();

        return baos.toByteArray();
    }


    private void adicionarCabecalho(Document document, String tipoRelatorio, String dataInicio, String dataFim) throws DocumentException {
        Paragraph header = new Paragraph("Relatório de " + tipoRelatorio, HEADER_FONT);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingAfter(10);
        document.add(header);

        Paragraph periodo = new Paragraph("Período: " + dataInicio + " a " + dataFim, CELL_FONT);
        periodo.setAlignment(Element.ALIGN_CENTER);
        periodo.setSpacingAfter(20);
        document.add(periodo);
    }

    private void adicionarRodape(Document document) throws DocumentException {
        Paragraph footer = new Paragraph("Relatório gerado em: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), CELL_FONT);
        footer.setAlignment(Element.ALIGN_RIGHT);
        footer.setSpacingBefore(20);
        document.add(footer);
    }

    private PdfPTable criarTabelaBaseadoNoTipo(String tipo, List<?> dados) throws DocumentException {
        switch (tipo.toLowerCase()) {
            case "doadores":
                return criarTabelaDoadores((List<Doador>) dados);
            case "doacoes":
                return criarTabelaDoacoes((List<Doacao>) dados);
            case "funcionarios":
                return criarTabelaFuncionarios((List<Usuario>) dados);
            case "parceiros":
                return criarTabelaParceiros((List<Doador>) dados);
            default:
                throw new IllegalArgumentException("Tipo de relatório não suportado: " + tipo);
        }
    }

    private PdfPTable criarTabelaDoadores(List<Doador> doadores) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{2, 4, 3, 4, 3});
        table.setWidthPercentage(100);

        addTableHeader(table, new String[]{
                "Tipo", "Nome/Razão Social", "Documento", "Email", "Data Cadastro"
        });

        for (Doador doador : doadores) {
            addCell(table, doador.getTipo().equals("PF") ? "Pessoa Física" : "Pessoa Jurídica");
            addCell(table, doador.getTipo().equals("PF") ? doador.getNomeCompleto() : doador.getRazaoSocial());
            addCell(table, doador.getDocumento());
            addCell(table, doador.getEmail());
            addCell(table, formatarData(doador.getDataCadastro()));
        }

        return table;
    }

    private PdfPTable criarTabelaDoacoes(List<Doacao> doacoes) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{2, 3, 4, 3, 4});
        table.setWidthPercentage(100);

        addTableHeader(table, new String[]{
                "Valor", "Data", "Doador", "Método de Pagamento", "Observações"
        });

        BigDecimal totalDoacoes = BigDecimal.ZERO;
        for (Doacao doacao : doacoes) {
            addCell(table, String.format("R$ %.2f", doacao.getValor()));
            addCell(table, formatarData(doacao.getData()));
            addCell(table, doacao.getDoador().getTipo().equals("PF") ?
                    doacao.getDoador().getNomeCompleto() :
                    doacao.getDoador().getRazaoSocial());
            addCell(table, doacao.getMetodoPagamento());
            addCell(table, doacao.getObservacao() != null ? doacao.getObservacao() : "-");

            totalDoacoes = totalDoacoes.add(doacao.getValor());
        }

        PdfPCell cellTotal = new PdfPCell(new Phrase(
                String.format("Total: R$ %.2f", totalDoacoes), HEADER_FONT));
        cellTotal.setColspan(5);
        cellTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTotal.setPadding(10);
        table.addCell(cellTotal);

        return table;
    }

    private PdfPTable criarTabelaFuncionarios(List<Usuario> funcionarios) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{4, 3, 4, 3, 3});
        table.setWidthPercentage(100);

        addTableHeader(table, new String[]{
                "Nome", "CPF", "Email", "Cargo", "Data Cadastro"
        });

        for (Usuario funcionario : funcionarios) {
            addCell(table, funcionario.getNome());
            addCell(table, funcionario.getCpf());
            addCell(table, funcionario.getEmail());
            addCell(table, funcionario.getCargo());
            addCell(table, formatarData(funcionario.getDataCadastro()));
        }

        return table;
    }

    private PdfPTable criarTabelaParceiros(List<Doador> parceiros) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{4, 3, 4, 3, 3, 3});
        table.setWidthPercentage(100);

        addTableHeader(table, new String[]{
                "Razão Social", "CNPJ", "Email", "Telefone", "Responsável", "Data Cadastro"
        });

        for (Doador parceiro : parceiros) {
            if (parceiro.getParceiro() != null && parceiro.getParceiro()) {
                addCell(table, parceiro.getRazaoSocial() != null ? parceiro.getRazaoSocial() : "Não informado");
                addCell(table, parceiro.getDocumento() != null ? parceiro.getDocumento() : "Não informado");
                addCell(table, parceiro.getEmail() != null ? parceiro.getEmail() : "Não informado");
                addCell(table, parceiro.getTelefone() != null ? parceiro.getTelefone() : "Não informado");
                addCell(table, parceiro.getResponsavel() != null ? parceiro.getResponsavel() : "Não informado");
                addCell(table, formatarData(parceiro.getDataCadastro()));
            }
        }

        return table;
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, TITLE_FONT));
            cell.setBackgroundColor(HEADER_COLOR);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
    }

    private void addCell(PdfPTable table, String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content, CELL_FONT));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private String formatarData(LocalDateTime data) {
        if (data == null) return "Não informado";
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
