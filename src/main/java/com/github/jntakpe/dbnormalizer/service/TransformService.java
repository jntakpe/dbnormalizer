package com.github.jntakpe.dbnormalizer.service;

import com.github.jntakpe.dbnormalizer.domain.FileInfos;
import com.github.jntakpe.dbnormalizer.domain.JoinFI;
import com.github.jntakpe.dbnormalizer.domain.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
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

    public Set<JoinFI> convertAll(Set<FileInfos> filesInfos) {
        Set<JoinFI> target = new LinkedHashSet<>(filesInfos.size());
        for (FileInfos fi : filesInfos) {
            List<Table> tables = new LinkedList<>();
            List<String> indexes = new LinkedList<>();
            for (Table table : fi.getTables()) tables.add(converTable(table));
            for (String index : fi.getIndexes()) indexes.add(convertIndex(index));
            FileInfos fileInfos = new FileInfos(fi.getPath());
            fileInfos.setTables(tables);
            fileInfos.setIndexes(indexes);
            target.add(new JoinFI(fi.getPath(), fi, fileInfos));
        }
        return target;
    }

    public String convert(String fileContent, JoinFI context) {
        FileInfos curFI = context.getCurrentFI();
        int size = context.getCurrentFI().getTables().size();
        for (int i = 0; i < size; i++) {
            fileContent = transformTable(fileContent, context, i);
        }
        return "";
    }

    private String transformTable(String fileContent, JoinFI context, int idx) {
        Table current = context.getCurrentFI().getTables().get(idx);
        Table target = context.getTargetFI().getTables().get(idx);
        fileContent = fileContent.replaceAll(current.getName(), target.getName());
        fileContent = fileContent.replaceAll(current.getPk(), target.getPk());
        int size = current.getColumns().size();
        for (int i = 0; i < size; i++)
            fileContent = fileContent.replaceAll(current.getColumns().get(i), target.getColumns().get(i));
        size = current.getConstraints().size();
        for (int i = 0; i < size; i++)
            fileContent = fileContent.replaceAll(current.getConstraints().get(i), target.getConstraints().get(i));
        size = current.getFks().size();
        for (int i = 0; i < size; i++)
            fileContent = fileContent.replaceAll(current.getFks().get(i), target.getFks().get(i));
        return fileContent;
    }

    private Table converTable(Table table) {
        Table target = new Table();
        target.setPrefix(parameterService.getTargetTablePrefix());
        target.setName(converTableName(table.getName()));
        target.setPk(convertPk(target.getPrefix()));
        target.setColumns(convertColumns(table.getColumns()));
        target.setConstraints(convertConstraint(table));
        return target;
    }

    private String converTableName(String name) {
        String rawName = name.substring(6);
        return "T_" + parameterService.getTargetTablePrefix() + "_" + rawName;
    }

    private String convertPk(String tablePrefix) {
        return parameterService.getTargetIdColumn() + tablePrefix;
    }

    private List<String> convertColumns(List<String> columns) {
        List<String> target = new LinkedList<>();
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

    private List<String> convertConstraint(Table table) {
        List<String> constraints = table.getConstraints();
        List<String> target = new LinkedList<>();
        for (String constraint : constraints) {
            if (constraint.startsWith("PK_")) target.add("PK_" + table.getName());
            else target.add(constraint.replace("UC_", "IX_"));
        }
        return target;
    }

    private String convertIndex(String index) {
        index = index.substring(0, index.lastIndexOf("_"));
        String col = "ID" + index.substring(index.lastIndexOf("_"));
        return index.substring(0, 7) + col;
    }

}
