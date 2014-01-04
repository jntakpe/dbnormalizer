package com.github.jntakpe.dbnormalizer.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Bean représentant une table
 *
 * @author jOSS
 */
public class Table {

    private String name;

    private String prefix;

    private Set<String> columns = new HashSet<>();

    private String pk;

    private Set<String> fks = new HashSet<>();;

    private Set<String> constraints = new HashSet<>();;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Set<String> getColumns() {
        return columns;
    }

    public void setColumns(Set<String> columns) {
        this.columns = columns;
    }

    public void addColumn(String column) {
        columns.add(column);
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public Set<String> getFks() {
        return fks;
    }

    public void setFks(Set<String> fks) {
        this.fks = fks;
    }

    public void addFk(String fk) {
        fks.add(fk);
    }

    public Set<String> getConstraints() {
        return constraints;
    }

    public void setConstraints(Set<String> constraints) {
        this.constraints = constraints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (!name.equals(table.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                '}';
    }
}
