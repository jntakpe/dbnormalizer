package com.github.jntakpe.dbnormalizer.processor;

import com.github.jntakpe.dbnormalizer.domain.Table;
import com.github.jntakpe.dbnormalizer.service.ParameterService;
import org.apache.commons.lang3.StringUtils;

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
            if (StringUtils.isBlank(line) || isEnd(line)) break;
            if (i == 0) {
                extractPK(line);
            } else {
                if (isConstraint(line)) extractConstraint(line);
                else {
                    if (isFK(line)) extractFK(line);
                    else extractColumn(line);
                }
            }
            i++;
        }
        return table;
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
        int idxEnd = tableName.indexOf("_", ++idxStart);
        return tableName.substring(idxStart, idxEnd);
    }

    private void extractPK(String line) {
        if (line.contains(parameterService.getCurrentIdColumn())) table.setPk(parameterService.getCurrentIdColumn());
    }

    private boolean isFK(String line) {
        return line.contains("NB_") && line.contains("_Id");
    }

    private void extractFK(String line) {
        int idxStart = line.indexOf("NB_");
        int idxEnd = line.indexOf("_Id") + "_Id".length();
        table.addFk(line.substring(idxStart, idxEnd));
    }

    private boolean isConstraint(String line) {
        return line.trim().startsWith("constraint");
    }

    private void extractColumn(String line) {
        line = line.trim();
        int endIdx = line.indexOf(" ");
        table.addColumn(line.substring(0, endIdx));
    }

    private void extractConstraint(String line) {
        line = line.trim();
        int startIdx = line.indexOf(" ");
        int endIdx = line.indexOf(" ", ++startIdx);
        table.addConstraint(line.substring(startIdx, endIdx));
    }

    private boolean isEnd(String line) {
        return line.trim().equals(")");
    }

}
