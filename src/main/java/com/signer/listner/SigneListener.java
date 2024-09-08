package com.signer.listener;

import com.google.protobuf.ByteString;
import com.signer.domain.proto.FileMessage;
import com.signer.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Component
public class SigneListener {

    private static final Logger log = LoggerFactory.getLogger(SigneListener.class);

    @RabbitListener(queues = "signed-002321")
    public void receive(byte[] message) {
        try {
            FileMessage fileMessage = FileMessage.parseFrom(message);

            String name = getStringAttributeByKey(fileMessage, Constants.NAME);
            byte[] content = getBytesAttributeByKey(fileMessage, Constants.CONTENT);
            byte[] signatureContent = getBytesAttributeByKey(fileMessage, Constants.SIGNATURE_CONTENT);

            if (content == null) {
                log.error("O conteúdo do arquivo está vazio ou não pôde ser lido.");
                return;
            }

            log.info("Recebido conteúdo: {}", Arrays.toString(content));
            String fileExtension = getExtensionFile(content);

            // Salva o conteúdo principal do arquivo
            try (FileOutputStream fileOutputStream = new FileOutputStream(name.concat(fileExtension))) {
                fileOutputStream.write(content);
            }

            // Salva a assinatura destacável, se existir
            if (Objects.nonNull(signatureContent)) {
                // Adiciona a extensão correta .xml.p7s para arquivos XML com assinatura destacada
                String signatureExtension = fileExtension.equals(Constants.XML) ? ".xml.p7s" : Constants.DETACHE_SIGNATURE + fileExtension;
                try (FileOutputStream outputStream = new FileOutputStream(name.concat(signatureExtension))) {
                    outputStream.write(signatureContent);
                }
            }

            log.info("Arquivos salvos com sucesso: {}{}", name, fileExtension);
            if (signatureContent != null) {
                log.info("Assinatura destacável salva com sucesso como: {}{}", name.concat(Constants.DETACHE_SIGNATURE), fileExtension);
            }

        } catch (IOException e) {
            log.error("Erro ao processar a mensagem assinada", e);
        }
    }

    private String getExtensionFile(byte[] content) {
        if (Arrays.equals(Arrays.copyOfRange(content, 0, Constants.PDF_MAGIC_NUMBER.length), Constants.PDF_MAGIC_NUMBER)) {
            return Constants.PDF;
        } else if (Arrays.equals(Arrays.copyOfRange(content, 0, Constants.XML_MAGIC_NUMBER.length), Constants.XML_MAGIC_NUMBER)) {
            return Constants.XML;
        } else {
            log.info("Tipo de arquivo desconhecido");
            return Constants.EMPTY;
        }
    }

    private String getStringAttributeByKey(FileMessage fileMessage, String key) {
        Map<String, ByteString> attributes = fileMessage.getAttributesMap();
        if (attributes.containsKey(key)) {
            return attributes.get(key).toStringUtf8();
        } else {
            log.warn("Chave '{}' não encontrada nos atributos", key);
            return null;
        }
    }

    private byte[] getBytesAttributeByKey(FileMessage fileMessage, String key) {
        Map<String, ByteString> attributes = fileMessage.getAttributesMap();
        if (attributes.containsKey(key)) {
            return attributes.get(key).toByteArray();
        } else {
            log.warn("Chave '{}' não encontrada nos atributos", key);
            return null;
        }
    }
}
