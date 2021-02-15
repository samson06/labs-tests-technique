/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : AppRootConfig.java
 * Date de création : 15 févr. 2021
 * Heure de création : 02:23:45
 * Package : fr.supraloglabs.jbe.config
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Configuration de base de l'application.
 * 
 * @author Vincent Otchoun
 */
@Configuration
@PropertySource(value = { "classpath:application.properties" }, ignoreResourceNotFound = false)
@ComponentScan(basePackages = { "fr.supraloglabs.jbe" })
@EntityScan("fr.supraloglabs.jbe.model.po")
@EnableMongoRepositories(basePackages = { "fr.supraloglabs.jbe.dao" })
@EnableTransactionManagement
public class AppRootConfig
{
    /**
     * Construire le bean de générateur de mappeur Jackson vers objet.
     * 
     * @return le générateur de mappeur.
     */
    @Bean(name = "jackson2ObjectMapperBuilder")
    @Primary
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder()
    {
        //
        return new Jackson2ObjectMapperBuilder()
        {
            //
            @Override
            public void configure(final ObjectMapper pObjectMapper)
            {
                //
                super.configure(pObjectMapper);

                pObjectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
                pObjectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
                pObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                pObjectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                pObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
                pObjectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
                pObjectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
                pObjectMapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
                pObjectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
                pObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
                pObjectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
                pObjectMapper.configure(SerializationFeature.WRAP_EXCEPTIONS, true);

                pObjectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
                pObjectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, true);
                pObjectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
            }
        };
    }

    /**
     * Construire le bean de mappeurd'objets de construction au format JSON.
     * 
     * @return le mappeur d'objets au format JSON.
     */
    @Bean(name = "objectMapper")
    @Primary
    public ObjectMapper objectMapper()
    {
        return this.jackson2ObjectMapperBuilder().build();
    }

    /**
     * mappes de propriétés personnalisées et convertisseurs pour gérer le transfert de l'objet de transfert de données
     * à l'entité et vice versa.
     * 
     * @return
     */
    @Bean(name = "modelMapper")
    @Primary
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }
}
