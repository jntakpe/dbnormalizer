package com.github.jntakpe.dbnormalizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service de lecture d'un répertoire
 *
 * @author jOSS
 */
@Service
public class DirReader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileReader fileReader;

    @Value("${input.dir}")
    private String inputDir;

    public void read() {
        Assert.notNull(inputDir, "Lecture d'entrée non renseigné");
        logger.info("Lecture du répertoire : {}", inputDir);
        Path dirPath = Paths.get(inputDir);
        Assert.isTrue(Files.exists(dirPath), "Le répertoire d'entrée n'existe pas");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.sql")) {
            for (Path filePath : stream)
                fileReader.read(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture du répertoire d'entrée", e);
        }
    }

}
