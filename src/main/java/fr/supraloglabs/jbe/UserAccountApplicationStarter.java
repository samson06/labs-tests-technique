/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserAccountApplicationStarter.java
 * Date de création : 14 févr. 2021
 * Heure de création : 17:03:43
 * Package : fr.supraloglabs.jbe
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Back-end REST Services boot starter.
 * 
 * @author Vincent Otchoun
 */
@SpringBootApplication
public class UserAccountApplicationStarter
{
    /**
     * Méthode principale de démarrage de l'application.
     * 
     * @param args les paramètres de démarrage de l'application.
     */
    public static void main(String... args)
    {
        SpringApplication.run(UserAccountApplicationStarter.class, args);
    }
}
