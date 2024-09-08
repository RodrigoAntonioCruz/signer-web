package com.signer.service;

import com.google.protobuf.ByteString;
import com.itextpdf.html2pdf.HtmlConverter;
import com.signer.domain.proto.FileMessage;
import com.signer.util.Constants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class SignerService {

    private final RabbitTemplate template;

    private final TissService tiss;

    public SignerService(RabbitTemplate template, TissService tiss) {
        this.template = template;
        this.tiss = tiss;
    }

    public String sendPDF(String name, String alias) {

        String htmlContent = """
                <html>
                <body>
                    <h1>Clínica Saúde Total</h1>
                    <p>Rua das Flores, 123, Bairro Central, São Paulo, SP, 01234-567, (11) 1234-5678</p>
                    <h2>PRONTUÁRIO DO PACIENTE</h2>
                    <p><strong>Número do prontuário:</strong> 2024-001</p>
                    <p><strong>Data de Abertura:</strong> 31/08/2024</p>
                    <p><strong>Nome completo:</strong> João da Silva Souza</p>
                    <p><strong>Data de nascimento:</strong> 15/03/1985</p>
                    <p><strong>Sexo:</strong> [X] M [ ] F</p>
                    <p><strong>Endereço:</strong> Rua dos Lírios, 456, Bairro Nova Esperança, São Paulo, SP, 09876-543</p>
                    <p><strong>Telefone(s):</strong> (11) 98765-4321</p>
                    <p><strong>E-mail:</strong> joao.silva@example.com</p>
                    <p><strong>Contato do responsável/cuidador:</strong> Maria da Silva Souza, (11) 91234-5678</p>
                    <p><strong>Médico(s) do Paciente:</strong> Dr. Carlos Pereira</p>
                    <p><strong>Escolaridade:</strong> Ensino Superior Completo</p>
                    <p><strong>Ocupação:</strong> Engenheiro Civil</p>
                    <p><strong>Limitação:</strong> [ ] Cognitiva [ ] Locomoção [ ] Visão [X] Audição [ ] Outras: Diabetes</p>
                    <p><strong>Alergia:</strong> Penicilina</p>
                    <h3>Aula nº 05 – ADMISSÃO, ALTA TRANSFERÊNCIA E PASSAGEM DE PLANTÃO</h3>
                    <p><strong>I. ADMISSÃO:</strong></p>
                    <p>É o processo que ocorre quando uma pessoa entra em uma instituição de cuidados à saúde para permanecer por mais de 24h para os cuidados e tratamento. Deve ser feita pelo enfermeiro.</p>
                    <p>A ADMISSÃO ENVOLVE QUATRO PROCESSOS:</p>
                    <p><strong>AUTORIZAÇÃO MÉDICA PRÉVIA:</strong> Atendimento de urgência / emergência;</p>
                    <!-- Continue com o conteúdo necessário -->
                </body>
                </html>
                """;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            HtmlConverter.convertToPdf(htmlContent, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        var message = buildFile(Constants.EMPTY, name, alias, outputStream.toByteArray());

        send(message);

        return "PDF gerado e enviado para assinatura.";
    }

    public String sendXML(String detached, String name, String alias) {
        try {
            String xmlContent = tiss.getXmlWithHash();
            String detachedSignature = Constants.EMPTY;

            byte[] xmlBytes = xmlContent.getBytes(StandardCharsets.ISO_8859_1);

            if (Objects.nonNull(detached)) {
                detachedSignature = detached;
            }

            FileMessage message = buildFile(detachedSignature, name, alias, xmlBytes);
            send(message);

            return "XML enviado para assinatura.";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private FileMessage buildFile(String key, String name, String alias, byte[] content) {
        return FileMessage.newBuilder()
                .putAttributes(Constants.KEY, ByteString.copyFromUtf8(key))
                .putAttributes(Constants.NAME, ByteString.copyFromUtf8(name))
                .putAttributes(Constants.ALIAS, ByteString.copyFromUtf8(alias))
                .putAttributes(Constants.CONTENT, ByteString.copyFrom(content))
                .build();
    }
    private void send(FileMessage message) {
        template.convertAndSend("to-sign-002321", message.toByteArray());
    }
}
