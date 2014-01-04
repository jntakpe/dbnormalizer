package com.github.jntakpe.dbnormalizer.service;

import com.github.jntakpe.dbnormalizer.domain.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Transforme les données en entrée par les données modifiés
 *
 * @author jOSS
 */
@Service
public class TransformService {

    @Autowired
    private ParameterService parameterService;

    public Table converTable(Table table) {
        Table target = new Table();
        target.setPrefix(parameterService.getTargetTablePrefix());
        target.setName(converTableName(table.getName()));
        target.setPk(convertPk(target.getName()));
        target.setConstraints(convertConstraint(table));
        return target;
    }

    private String converTableName(String name) {
        String rawName = name.substring(6);
        return "T_" + parameterService.getTargetTablePrefix() + rawName;
    }

    private String convertPk(String tableName) {
        return parameterService.getTargetIdColumn() + "_" + tableName;
    }

    private Set<String> convertColumns(Set<String> columns) {
        Set<String> target = new HashSet<>(columns.size());
        String curChar = parameterService.getCurrentCharPrefix();
        String curVarchar = parameterService.getCurrentVarcharPrefix();
        String targetChar = parameterService.getTargetCharPrefix();
        String targetVarchar = parameterService.getTargetVarcharPrefix();
        for (String column : columns) {
            if (column.startsWith(curChar)) target.add(column.replace(curChar, targetChar));
            else if (column.startsWith(curVarchar)) target.add(column.replace(curVarchar, targetVarchar));
            else target.add(column);
        }
        return target;
    }

    private Set<String> convertConstraint(Table table) {
        Set<String> constraints = table.getConstraints();
        Set<String> target = new HashSet<>(constraints.size());
        for (String constraint : constraints) {
            if (constraint.startsWith("PK_"))target.add("PK_" + table.getName());
            else target.add(constraint.replace("UC_", "IX_"));
        }
        return target;
    }
}
