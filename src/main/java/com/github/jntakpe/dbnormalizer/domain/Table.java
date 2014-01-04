package com.github.jntakpe.dbnormalizer.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Bean représentant une table
 *
 * @author jOSS
 */
public class Table {

    private String name;

    private String prefix;

    private List<String> columns = new LinkedList<>();

    private String pk;

    private List<String> fks = new LinkedList<>();;

    private List<String> constraints = new LinkedList<>();;

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

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
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

    public List<String> getFks() {
        return fks;
    }

    public void setFks(List<String> fks) {
        this.fks = fks;
    }

    public void addFk(String fk) {
        fks.add(fk);
    }

    public List<String> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<String> constraints) {
        this.constraints = constraints;
    }

    public void addConstraint(String constraint) {
        constraints.add(constraint);
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
