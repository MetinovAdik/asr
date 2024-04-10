package com.metinov.asr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class ASRController {

    private final ASRservice asrService;

    @Autowired
    public ASRController(ASRservice asrService) {
        this.asrService = asrService;
    }

    @PostMapping("/transcribe")
    public ResponseEntity<String> transcribeAudio(@RequestParam("audioFile") MultipartFile audioFile) {
        if (audioFile.isEmpty()) {
            return ResponseEntity.badRequest().body("No audio file provided");
        }

        try {
            Path tempFile = Files.createTempFile(null, null);
            audioFile.transferTo(tempFile);

            asrService.speechRecognition(tempFile);

            Files.deleteIfExists(tempFile);

            return ResponseEntity.ok("Transcription completed successfully");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error during transcription: " + e.getMessage());
        }
    }
}
