/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserAccountApplicationWeb.java
 * Date de création : 14 févr. 2021
 * Heure de création : 17:13:05
 * Package : fr.supraloglabs.jbe
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Ajout du profile web pour faciliter le déploiement et l'exécution dans un container externe.
 * 
 * @author Vincent Otchoun
 */
public class UserAccountApplicationWeb extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
    {
        return builder.sources(UserAccountApplicationStarter.class);
    }
}
