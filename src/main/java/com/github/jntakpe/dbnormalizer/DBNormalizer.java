package com.github.jntakpe.dbnormalizer;

import com.github.jntakpe.dbnormalizer.service.DirReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        DirReader dirReader = ac.getBean(DirReader.class);
        dirReader.read();
    }



}
