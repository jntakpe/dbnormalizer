package com.github.jntakpe.dbnormalizer.service;

import org.springframework.beans.factory.annotation.Value;

/**
 * Service de gestion des paramètres
 *
 * @author jOSS
 */
public class ParameterService {

    @Value("${input.dir}")
    private String inputDir;

    @Value("${output.dir}")
    private String outputDir;

    @Value("${current.table.prefix}")
    private String currentTablePrefix;

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

}
