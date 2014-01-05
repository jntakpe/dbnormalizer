package com.github.jntakpe.dbnormalizer.service;

import com.github.jntakpe.dbnormalizer.domain.FileInfos;
import com.github.jntakpe.dbnormalizer.domain.JoinFI;
import com.github.jntakpe.dbnormalizer.domain.Table;
import org.apache.commons.lang3.StringUtils;
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
        int size = context.getCurrentFI().getTables().size();
        for (int i = 0; i < size; i++) {
            fileContent = transformTable(fileContent, context, i);
        }
        size = context.getCurrentFI().getIndexes().size();
        for (int i = 0; i < size; i++) {
            fileContent = transformIndex(fileContent, context, i);
        }
        fileContent = addVersion(fileContent);
        return fileContent;
    }

    private String transformTable(String fileContent, JoinFI context, int idx) {
        Table current = context.getCurrentFI().getTables().get(idx);
        Table target = context.getTargetFI().getTables().get(idx);
        fileContent = fileContent.replaceAll(current.getName(), target.getName());
        if (current.getPk() != null) fileContent = transformPk(fileContent, target);
        int size = current.getColumns().size();
        if (size != 0) {
            for (int i = 0; i < size; i++)
                fileContent = fileContent.replaceAll(current.getColumns().get(i), target.getColumns().get(i));
        }
        size = current.getConstraints().size();
        if (size != 0) {
            for (int i = 0; i < size; i++)
                fileContent = fileContent.replaceAll(current.getConstraints().get(i), target.getConstraints().get(i));
        }
        size = current.getFks().size();
        if (size != 0) {
            for (int i = 0; i < size; i++)
                fileContent = fileContent.replaceAll(current.getFks().get(i), target.getFks().get(i));
        }
        return fileContent;
    }

    private String transformPk(String fileContent, Table target) {
        String currentName = target.getName() + " (" + System.getProperty("line.separator") + "   NB_Id";
        String targetName = target.getName() + " (" + System.getProperty("line.separator") + "   " + target.getPk();
        return StringUtils.replace(fileContent, currentName, targetName);
    }

    private String transformIndex(String fileContent, JoinFI context, int idx) {
        String current = context.getCurrentFI().getIndexes().get(idx);
        String target = context.getTargetFI().getIndexes().get(idx);
        return fileContent.replaceAll(current, target);
    }

    private Table converTable(Table table) {
        Table target = new Table();
        target.setPrefix(parameterService.getTargetTablePrefix());
        target.setName(converTableName(table.getName()));
        target.setPk(convertPk(table.getPrefix()));
        target.setColumns(convertColumns(table.getColumns()));
        target.setConstraints(convertConstraint(table, target));
        target.setFks(convertFks(table));
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

    private List<String> convertConstraint(Table table, Table tar) {
        List<String> constraints = table.getConstraints();
        List<String> target = new LinkedList<>();
        for (String constraint : constraints) {
            if (constraint.startsWith("PK_")) target.add("PK_" + tar.getName());
            else target.add(constraint.replace("UC_", "IX_"));
        }
        return target;
    }

    private String convertIndex(String index) {
        index = index.substring(0, index.lastIndexOf("_"));
        String col = "ID" + index.substring(index.lastIndexOf("_"));
        return index.substring(0, 6) + "_" + col;
    }

    private List<String> convertFks(Table table) {
        List<String> fks = new LinkedList<>();
        for (String fk : table.getFks()) {
            String prefix = fk.substring(fk.indexOf("_") + 1, fk.lastIndexOf("_"));
            fks.add("ID_" + prefix);
        }
        return fks;
    }

    private String addVersion(String fileContent) {
        return fileContent.replaceAll("constraint PK_", "   LB_AgentCreation     varchar(50)          not null,\n" +
                "   LB_AgentDerniereMaj  varchar(50)          not null,\n" +
                "   DT_Creation          datetime             not null,\n" +
                "   DT_DerniereMaj       datetime             not null,\n" +
                "   constraint PK_");
    }

}
