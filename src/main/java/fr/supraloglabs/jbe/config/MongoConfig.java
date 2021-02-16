/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : MongoConfig.java
 * Date de création : 16 févr. 2021
 * Heure de création : 08:12:29
 * Package : fr.supraloglabs.jbe.config
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import fr.supraloglabs.jbe.util.UserAccountUtil;

/**
 * Configuration de beans de la couche abstraite d'accès aux informations en abse de données.
 * 
 * @author Vincent Otchoun
 */
@Import(value = { AppRootConfig.class })
@Configuration
@EnableTransactionManagement
public class MongoConfig
{
    // Injection des propriétés externalisées
    @Value("${spring.application.name}")
    private String proAppName;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private Integer port;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Primary
    @Bean(name = "mongoClient")
    public MongoClient mongoClient()
    {
        // // Construire la chaîne de connection
        final String urlConnection = UserAccountUtil.mongoDBConnectionStr(this.host, this.port, this.database);

        // Credentials dans le cas où la connexion est sécurisée
        final MongoCredential mongoCredential = MongoCredential.createCredential(this.username, this.database, this.password.toCharArray());

        final ConnectionString connectionString = new ConnectionString(urlConnection);
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()//
        .applicationName(this.proAppName)//
        .credential(mongoCredential)//
        .writeConcern(WriteConcern.ACKNOWLEDGED)//
        .applyConnectionString(connectionString)//
        .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Primary
    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate()
    {
        return new MongoTemplate(mongoClient(), this.database);
    }

    /**
     * Déclaration du validateur.
     * 
     * @return l'instance de validateur.
     */
    @Primary
    @Bean(name = "validatorFactoryBean")
    public LocalValidatorFactoryBean validatorFactoryBean()
    {
        return new LocalValidatorFactoryBean();
    }

    /**
     * Ecouteurs d'événements de validation.
     * 
     * @return l'instance de l'écouteur d'événements de validation.
     */
    @Primary
    @Bean(name = "validatingMongoEventListener")
    public ValidatingMongoEventListener validatingMongoEventListener()
    {
        return new ValidatingMongoEventListener(validatorFactoryBean());
    }

    /**
     * Obtenir le traduction d'exceptions de persistance.
     *
     * @return le traduction des exceptions de persistance.
     */
    @Primary
    @Bean(name = "exceptionTranslationPostProcessor")
    public PersistenceExceptionTranslationPostProcessor exceptionTranslationPostProcessor()
    {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
