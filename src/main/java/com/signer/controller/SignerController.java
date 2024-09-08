package com.signer.controller;

import com.signer.service.SignerService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
@RestController
public class SignerController {

    private final SignerService signerService;

    public SignerController(SignerService signerService) {
        this.signerService = signerService;
    }
    private static final String FILE_DIRECTORY = "C:/Users/Rodrigo/Desktop/signer-web";
    @GetMapping("/pdf/{name}/{alias}")
    public String sendPdf(@PathVariable String name,
                          @PathVariable String alias) {
        return signerService.sendPDF(name, alias);
    }

    @GetMapping("/xml/{detached}/{name}/{alias}")
    public String sendXML(@PathVariable String detached,
                          @PathVariable String name,
                          @PathVariable String alias) {
        return signerService.sendXML(detached, name, alias);
    }

    @GetMapping("/xml/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable("fileName") String fileName) {
        try {
            File file = new File(FILE_DIRECTORY + File.separator + fileName);

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .body(resource);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
