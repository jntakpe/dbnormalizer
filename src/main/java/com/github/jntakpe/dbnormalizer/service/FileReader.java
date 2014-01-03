package com.github.jntakpe.dbnormalizer.service;

import com.github.jntakpe.dbnormalizer.domain.Table;
import com.github.jntakpe.dbnormalizer.processor.TableBlockProcessor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Service de lecture d'un fichier
 *
 * @author jOSS
 */
@Service
public class FileReader {

    public void read(Path path) {
        Table table = new Table();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
            String line;
            TableBlockProcessor tableBlockProcessor = null;
            while ((line = reader.readLine()) != null) {
                if (isTableLine(line)) {
                    tableBlockProcessor = new TableBlockProcessor(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture du fichier : " + path, e);
        }
    }

    private boolean isTableLine(String line) {
        return line != null && line.contains("create table");
    }
}
