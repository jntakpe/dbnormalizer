package com.github.jntakpe.dbnormalizer.domain;

import java.nio.file.Path;
import java.util.List;

/**
 * Objet contenant toutes les informations récupérées depuis un fichier
 *
 * @author jOSS
 */
public class FileInfos {

    private final Path path;

    private List<Table> tables;

    private List<String> indexes;

    public FileInfos(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public List<String> getIndexes() {
        return indexes;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public void setIndexes(List<String> indexes) {
        this.indexes = indexes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileInfos fileInfos = (FileInfos) o;

        if (!path.equals(fileInfos.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return "FileInfos{" +
                "path='" + path + '\'' +
                '}';
    }
}
