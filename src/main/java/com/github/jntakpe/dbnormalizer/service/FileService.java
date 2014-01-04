package com.github.jntakpe.dbnormalizer.service;

import com.github.jntakpe.dbnormalizer.domain.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Services à la lecture et écriture d'un fichier
 *
 * @author jOSS
 */
@Service
public class FileService {

    @Autowired
    private ParameterService parameterService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${input.dir}")
    private String inputDir;

    public Set<Table> findSQLScripts() {
        Assert.notNull(inputDir, "Lecture d'entrée non renseigné");
        logger.info("Lecture du répertoire : {}", inputDir);
        Path dirPath = Paths.get(inputDir);
        Assert.isTrue(Files.exists(dirPath), "Le répertoire d'entrée n'existe pas");
        Set<Table> tables = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.sql")) {
            for (Path filePath : stream)
                tables.addAll(read(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture du répertoire d'entrée", e);
        }
        return tables;
    }

    public Set<Table> read(Path path) {
        logger.info("Lecture du fichier {}", path);
        Set<Table> tables = new HashSet<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
            String line;
            TableBlockProcessor tableBlockProcessor;
            while ((line = reader.readLine()) != null) {
                if (isTableLine(line)) {
                    tableBlockProcessor = new TableBlockProcessor(reader, line, parameterService);
                    tables.add(tableBlockProcessor.process());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture du fichier : " + path, e);
        }
        logger.info("Fin de la lecture du fichier {}", path);
        return tables;
    }

    private boolean isTableLine(String line) {
        return line != null && line.contains("create table");
    }
}
