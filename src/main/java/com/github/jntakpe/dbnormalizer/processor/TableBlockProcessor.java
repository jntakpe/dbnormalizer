package com.github.jntakpe.dbnormalizer.processor;

import com.github.jntakpe.dbnormalizer.domain.Table;
import com.github.jntakpe.dbnormalizer.service.ParameterService;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Lit un bloc de création de table d'un fichier .sql
 *
 * @author jOSS
 */
public final class TableBlockProcessor {

    private final Table table = new Table();

    private final BufferedReader reader;

    private final ParameterService parameterService;

    public TableBlockProcessor(BufferedReader reader, String line, ParameterService parameterService) {
        this.reader = reader;
        this.parameterService = parameterService;
        extractTableName(line);
    }

    public Table process() throws IOException {
        String line;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) break;
            if (i == 0) extractPK(line);
            else {

            }
            i++;
        }
    }

    private void extractTableName(String line) {
        int idxStart = "create table ".length();
        int idxEnd = line.indexOf("(");
        String tableName = line.substring(idxStart, idxEnd - 1);
        table.setName(tableName);
        table.setPrefix(extractPrefix(tableName));
    }

    private String extractPrefix(String tableName) {
        int idxStart = tableName.indexOf("_");
        int idxEnd = tableName.indexOf("_", idxStart);
        return tableName.substring(idxStart, idxEnd);
    }

    private String extractPK(String line) {
        return line.contains(parameterService.getCurrentIdColumn()) ? parameterService.getCurrentIdColumn() : null;
    }

}
