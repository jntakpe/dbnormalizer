package com.github.jntakpe.dbnormalizer.service;

import com.github.jntakpe.dbnormalizer.domain.FileInfos;
import com.github.jntakpe.dbnormalizer.domain.JoinFI;
import com.github.jntakpe.dbnormalizer.domain.Table;
import org.apache.commons.lang3.StringUtils;
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
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
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

    @Autowired
    private TransformService transformService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${input.dir}")
    private String inputDir;

    public Set<FileInfos> readDir() {
        Assert.notNull(inputDir, "Lecture d'entrée non renseigné");
        logger.info("Lecture du répertoire : {}", inputDir);
        Path dirPath = Paths.get(inputDir);
        Assert.isTrue(Files.exists(dirPath), "Le répertoire d'entrée n'existe pas");
        Set<FileInfos> filesInfos = new LinkedHashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.sql")) {
            for (Path filePath : stream)
                filesInfos.add(read(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture du répertoire d'entrée", e);
        }
        logger.info("Fin de la lecture du répertoire : {}", inputDir);
        return filesInfos;
    }

    public void transformDir(Set<JoinFI> context) {
        logger.info("Transformation du contenu du répertoire : {}", inputDir);
        Path dirPath = Paths.get(inputDir);
        for (JoinFI fi : context) {
            transformService.convert(readAsString(fi.getPath()), fi);
        }
        logger.info("Fin de la transformation du contenu du répertoire : {}", inputDir);
    }

    private String readAsString(Path path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture du fichier : " + path, e);
        }
        return StringUtils.join(lines, System.getProperty("line.separator"));
    }

    private FileInfos read(Path path) {
        logger.info("Lecture du fichier {}", path);
        FileInfos fileInfos = new FileInfos(path);
        List<Table> tables = new LinkedList<>();
        List<String> indexes = new LinkedList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
            String line;
            TableBlockProcessor tableBlockProcessor;
            while ((line = reader.readLine()) != null) {
                if (isTableLine(line)) {
                    tableBlockProcessor = new TableBlockProcessor(reader, line, parameterService);
                    tables.add(tableBlockProcessor.process());
                } else if (isIndexLine(line)) indexes.add(extractIndex(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture du fichier : " + path, e);
        }
        fileInfos.setTables(tables);
        fileInfos.setIndexes(indexes);
        logger.info("Fin de la lecture du fichier {}", path);
        return fileInfos;
    }

    private boolean isTableLine(String line) {
        return line != null && line.contains("create table");
    }

    private boolean isIndexLine(String line) {
        return line.startsWith("create index IX_");
    }

    private String extractIndex(String line) {
        return line.substring("create index ".length(), line.indexOf("on T")).trim();
    }
}
