package com.github.jntakpe.dbnormalizer;

import com.github.jntakpe.dbnormalizer.domain.Table;
import com.github.jntakpe.dbnormalizer.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Set;

/**
 * Classe mère de l'application
 *
 * @author jOSS
 */
public class DBNormalizer {

    private static Logger LOGGER = LoggerFactory.getLogger(DBNormalizer.class);

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/spring-context.xml");
        LOGGER.debug("Contexte Spring démarré");
        FileService fileService = ac.getBean(FileService.class);
        Set<Table> tables = fileService.findSQLScripts(); //Lecture des fichiers

        LOGGER.info("Fin des traitements");
    }


}
