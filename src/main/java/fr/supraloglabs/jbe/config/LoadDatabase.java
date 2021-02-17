/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : LoadDatabase.java
 * Date de création : 17 févr. 2021
 * Heure de création : 23:09:51
 * Package : fr.supraloglabs.jbe.config
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import fr.supraloglabs.jbe.model.po.User;
import fr.supraloglabs.jbe.repository.UserRepository;
import fr.supraloglabs.jbe.service.user.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration pour initialiser la base de données avec le jeu de données de tests.
 * 
 * @author Vincent Otchoun
 */
@Configuration
@Slf4j
public class LoadDatabase
{
    @Autowired
    private UserRepository userRepository;

    private UserService userService;
    
    @PostConstruct
    private void init() {
        this.userService = new UserService(this.userRepository);
    }

    @Bean
    // CommandLineRunner initUsers(final UserService userService)
    CommandLineRunner initUsers()
    {
        return args -> {
            log.info("[initUsers] - Initialisation de la base de données avec le jeu de tests. ");
            this.creerJeuDeDonnees().forEach(this.userService::createUser);
        };
    }

    private List<User> creerJeuDeDonnees()
    {
        final List<User> users = Lists.newArrayList();
        users.add(user);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        return users;
    }

    private static final String PAYS = "France";
    private User user = User.builder()//
    .firstName("Batcho")//
    .lastName("Karen Djayé")//
    .email("karen.test@live.fr")//
    .age(20)//
    .country(PAYS)//
    .adresse("23 Rue du Commandant Mages")//
    .city("Marseille")//
    .phone("0645789512")//
    .build();

    private User user2 = User.builder()//
    .firstName("Adjba")//
    .lastName("Italé Naldine")//
    .email("naldine.test@live.fr")//
    .age(28)//
    .country(PAYS)//
    .adresse("13 Rue Lakanal")//
    .city("Toulouse")//
    .phone("0645789912")//
    .build();

    private User user3 = User.builder()//
    .firstName("Dessoum")//
    .lastName("André Marcel")//
    .email("marcel.test@live.fr")//
    .age(36)//
    .country(PAYS)//
    .adresse("1 Boulevard Wilson")//
    .city("Antibes")//
    .phone("0745788912")//
    .build();

    private User user4 = User.builder()//
    .firstName("Belahy")//
    .lastName("Steeve")//
    .email("steeve.test@live.fr")//
    .age(32)//
    .country(PAYS)//
    .adresse("1 Boulevard Wilson")//
    .city("Juan Les Pins")//
    .phone("0759733912")//
    .build();
}
