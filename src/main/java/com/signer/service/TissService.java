package com.signer.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class TissService {
    public String getXmlWithHash() throws Exception {
        // Gerar o XML base sem o hash
        String xmlContent = generateTISSXml();

        // Extrair os valores das tags preenchidas e calcular o hash MD5
        String concatenatedValues = extractValuesForHash(xmlContent);
        String md5Hash = calculateMD5Hash(concatenatedValues);

        String xmlWithHash = insertHashIntoXml(xmlContent, md5Hash);

        return xmlWithHash;
    }

    private static String generateTISSXml() {
        // Gerar o XML padrão TISS sem o hash
        return "<?xml version='1.0' encoding='ISO-8859-1' ?>"
                + "<ans:mensagemTISS xmlns:ans='http://www.ans.gov.br/padroes/tiss/schemas' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "xsi:schemaLocation='http://www.ans.gov.br/padroes/tiss/schemas http://www.ans.gov.br/padroes/tiss/schemas/tissV4_01_00.xsd'>"
                + "<ans:cabecalho><ans:identificacaoTransacao><ans:tipoTransacao>ENVIO_LOTE_GUIAS</ans:tipoTransacao>"
                + "<ans:sequencialTransacao>2</ans:sequencialTransacao>"
                + "<ans:dataRegistroTransacao>2024-06-11</ans:dataRegistroTransacao>"
                + "<ans:horaRegistroTransacao>17:38:30</ans:horaRegistroTransacao></ans:identificacaoTransacao>"
                + "<ans:origem><ans:identificacaoPrestador><ans:codigoPrestadorNaOperadora>123456</ans:codigoPrestadorNaOperadora>"
                + "</ans:identificacaoPrestador></ans:origem><ans:destino><ans:registroANS>322504</ans:registroANS></ans:destino>"
                + "<ans:Padrao>4.01.00</ans:Padrao></ans:cabecalho><ans:prestadorParaOperadora><ans:loteGuias><ans:numeroLote>2</ans:numeroLote>"
                + "<ans:guiasTISS><ans:guiaSP-SADT><ans:cabecalhoGuia><ans:registroANS>322504</ans:registroANS>"
                + "<ans:numeroGuiaPrestador>0000000002</ans:numeroGuiaPrestador><ans:guiaPrincipal>teste</ans:guiaPrincipal>"
                + "</ans:cabecalhoGuia><ans:dadosAutorizacao><ans:dataAutorizacao>2024-06-11</ans:dataAutorizacao>"
                + "<ans:senha>12345</ans:senha><ans:dataValidadeSenha>2025-07-01</ans:dataValidadeSenha></ans:dadosAutorizacao>"
                + "<ans:dadosBeneficiario><ans:numeroCarteira>322652626</ans:numeroCarteira><ans:atendimentoRN>N</ans:atendimentoRN>"
                + "</ans:dadosBeneficiario><ans:dadosSolicitante><ans:contratadoSolicitante><ans:codigoPrestadorNaOperadora>653.045.160-95</ans:codigoPrestadorNaOperadora>"
                + "</ans:contratadoSolicitante><ans:nomeContratadoSolicitante>Karin Ven</ans:nomeContratadoSolicitante>"
                + "<ans:profissionalSolicitante><ans:nomeProfissional>Karin Ven</ans:nomeProfissional>"
                + "<ans:conselhoProfissional>08</ans:conselhoProfissional><ans:numeroConselhoProfissional>20963</ans:numeroConselhoProfissional>"
                + "<ans:UF>43</ans:UF><ans:CBOS>223268</ans:CBOS></ans:profissionalSolicitante></ans:dadosSolicitante>"
                + "<ans:dadosSolicitacao><ans:dataSolicitacao>2024-06-12</ans:dataSolicitacao><ans:caraterAtendimento>1</ans:caraterAtendimento>"
                + "<ans:indicacaoClinica>Teste</ans:indicacaoClinica></ans:dadosSolicitacao><ans:dadosExecutante>"
                + "<ans:contratadoExecutante><ans:codigoPrestadorNaOperadora>123456</ans:codigoPrestadorNaOperadora></ans:contratadoExecutante>"
                + "<ans:CNES>21223</ans:CNES></ans:dadosExecutante><ans:dadosAtendimento><ans:tipoAtendimento>04</ans:tipoAtendimento>"
                + "<ans:indicacaoAcidente>9</ans:indicacaoAcidente><ans:regimeAtendimento>01</ans:regimeAtendimento>"
                + "</ans:dadosAtendimento><ans:procedimentosExecutados><ans:procedimentoExecutado><ans:sequencialItem>1</ans:sequencialItem>"
                + "<ans:dataExecucao>2024-06-12</ans:dataExecucao><ans:horaInicial>00:00:00</ans:horaInicial><ans:horaFinal>00:00:00</ans:horaFinal>"
                + "<ans:procedimento><ans:codigoTabela>22</ans:codigoTabela><ans:codigoProcedimento>10101012</ans:codigoProcedimento>"
                + "<ans:descricaoProcedimento>Em consultorio (no horario normal ou preestabelecido) cons</ans:descricaoProcedimento>"
                + "</ans:procedimento><ans:quantidadeExecutada>1</ans:quantidadeExecutada><ans:reducaoAcrescimo>1</ans:reducaoAcrescimo>"
                + "<ans:valorUnitario>120.00</ans:valorUnitario><ans:valorTotal>120.00</ans:valorTotal></ans:procedimentoExecutado>"
                + "</ans:procedimentosExecutados><ans:valorTotal><ans:valorProcedimentos>120.00</ans:valorProcedimentos>"
                + "<ans:valorDiarias>0.00</ans:valorDiarias><ans:valorTaxasAlugueis>0.00</ans:valorTaxasAlugueis>"
                + "<ans:valorMateriais>0.00</ans:valorMateriais><ans:valorMedicamentos>0.00</ans:valorMedicamentos>"
                + "<ans:valorOPME>0.00</ans:valorOPME><ans:valorGasesMedicinais>0.00</ans:valorGasesMedicinais>"
                + "<ans:valorTotalGeral>120.00</ans:valorTotalGeral></ans:valorTotal></ans:guiaSP-SADT></ans:guiasTISS></ans:loteGuias>"
                + "</ans:prestadorParaOperadora><ans:epilogo><ans:hash></ans:hash></ans:epilogo></ans:mensagemTISS>";
    }

    public String extractValuesForHash(String xml) {
        StringBuilder result = new StringBuilder();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();

            traverseNode(doc.getDocumentElement(), result);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private void traverseNode(Node node, StringBuilder result) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            result.append(node.getNodeValue());
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                traverseNode(nodeList.item(i), result);
            }
        }
    }

    private static String calculateMD5Hash(String content) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(content.getBytes(StandardCharsets.ISO_8859_1));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static String insertHashIntoXml(String xmlContent, String md5Hash) {
        return xmlContent.replace("<ans:hash></ans:hash>", "<ans:hash>" + md5Hash + "</ans:hash>");
    }
}