package com.github.jntakpe.dbnormalizer.processor;

import com.github.jntakpe.dbnormalizer.domain.Table;

/**
 * Lit un bloc de création de table d'un fichier .sql
 *
 * @author jOSS
 */
public final class TableBlockProcessor {

    private Table table;

    public TableBlockProcessor(String line) {
        table = new Table();
        table.setName(extractTableName(line));
    }

    private String extractTableName(String line) {
        return "toto";
    }

    public Table getTable() {
        return table;
    }

}
