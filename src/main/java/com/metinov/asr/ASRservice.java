package com.metinov.asr;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Service
public class ASRservice {

    public void speechRecognition(Path audioPath) throws IOException, InterruptedException{
        String command = String.format("python transcribe.py %s", audioPath.toString());
        ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        StringBuilder transcription = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isResultStart = false; // Флаг для отслеживания начала результата транскрипции
            while ((line = reader.readLine()) != null) {
                if (line.contains("—")) { // Предполагаем, что результат начинается после строки с "—"
                    isResultStart = true;
                }
                if (isResultStart) {
                    transcription.append(line).append("\n"); // Добавляем строку к результату
                }
            }
        }
        System.out.println(transcription.toString().trim());
    }

}
