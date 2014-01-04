package com.github.jntakpe.dbnormalizer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service de gestion des paramètres
 *
 * @author jOSS
 */
@Service
public class ParameterService {

    @Value("${input.dir}")
    private String inputDir;

    @Value("${output.dir}")
    private String outputDir;

    @Value("${target.table.prefix}")
    private String targetTablePrefix;

    @Value("${current.id.column}")
    private String currentIdColumn;

    @Value("${target.id.column}")
    private String targetIdColumn;

    @Value("${current.char.prefix}")
    private String currentCharPrefix;

    @Value("${target.char.prefix}")
    private String targetCharPrefix;

    @Value("${current.varchar.prefix}")
    private String currentVarcharPrefix;

    @Value("${target.varchar.prefix}")
    private String targetVarcharPrefix;

    public String getInputDir() {
        return inputDir;
    }

    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getTargetTablePrefix() {
        return targetTablePrefix;
    }

    public void setTargetTablePrefix(String targetTablePrefix) {
        this.targetTablePrefix = targetTablePrefix;
    }

    public String getCurrentIdColumn() {
        return currentIdColumn;
    }

    public void setCurrentIdColumn(String currentIdColumn) {
        this.currentIdColumn = currentIdColumn;
    }

    public String getTargetIdColumn() {
        return targetIdColumn;
    }

    public void setTargetIdColumn(String targetIdColumn) {
        this.targetIdColumn = targetIdColumn;
    }

    public String getCurrentCharPrefix() {
        return currentCharPrefix;
    }

    public void setCurrentCharPrefix(String currentCharPrefix) {
        this.currentCharPrefix = currentCharPrefix;
    }

    public String getTargetCharPrefix() {
        return targetCharPrefix;
    }

    public void setTargetCharPrefix(String targetCharPrefix) {
        this.targetCharPrefix = targetCharPrefix;
    }

    public String getCurrentVarcharPrefix() {
        return currentVarcharPrefix;
    }

    public void setCurrentVarcharPrefix(String currentVarcharPrefix) {
        this.currentVarcharPrefix = currentVarcharPrefix;
    }

    public String getTargetVarcharPrefix() {
        return targetVarcharPrefix;
    }

    public void setTargetVarcharPrefix(String targetVarcharPrefix) {
        this.targetVarcharPrefix = targetVarcharPrefix;
    }
}
