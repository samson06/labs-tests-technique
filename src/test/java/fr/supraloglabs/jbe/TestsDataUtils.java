/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : TestsDataUtils.java
 * Date de création : 16 févr. 2021
 * Heure de création : 11:34:36
 * Package : fr.supraloglabs.jbe
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import fr.supraloglabs.jbe.model.dto.UserDTO;
import fr.supraloglabs.jbe.model.po.User;
import lombok.experimental.UtilityClass;

/**
 * Utilitaire de tests.
 * 
 * @author Vincent Otchoun
 */
@UtilityClass
public class TestsDataUtils
{
    // Erreur HTTP
    public static final String URL_ERROR = "Impossible de trouver la méthode '%s' pour l'URL '%s'.";
    public static final String METHOD_ERROR = "Le paramètre '%s' de la valeur '%s' n'a pas pu être converti en type '%s'";
    public static final String HTTP_CLIENT_ERROR = "Erreur client HTTP.";
    public static final String SERVER_INTERNAL_ERROR = "Erreur interne du serveur.";
    public static final String FORMAT_ERROR = "Erreur format pour lecture et écriture.";
    public static final String CONTRAINST_VALDATION_ERROR = "Erreur violation de contraintes.";
    public static final String NOT_FOUND_ERROR = "Erreur recherche infructueuse de données.";
    public static final String INTEGRITY_ERROR = "Erreur de violations d'intégrité des données.";
    public static final String ACCESS_DENIED = "Accès non autorisés.";

    //
    public final UserDTO USER_DTO_TEST = UserDTO.builder()//
    // .id(generateUniqueId())//
    .lastName("Batcho")//
    .firstName("Karen Djayé")//
    .email("karen.test@live.fr")//
    .age(28)//
    .country("France")//
    .adresse("23 Rue du Commandant Mages")//
    .city("Marseille")//
    .phone("0645789512")//
    .build();

    public static void assertAllUserDTO(final UserDTO expected, final UserDTO actual)
    {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
        assertThat(actual.getCountry()).isEqualTo(expected.getCountry());
        assertThat(actual.getAdresse()).isEqualTo(expected.getAdresse());
        assertThat(actual.getCity()).isEqualTo(expected.getCity());
        assertThat(actual.getPhone()).isEqualTo(expected.getPhone());
    }

    public static void assertAllUsersUserAndUserDTO(final User expected, final UserDTO actual)
    {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
        assertThat(actual.getCountry()).isEqualTo(expected.getCountry());
        assertThat(actual.getAdresse()).isEqualTo(expected.getAdresse());
        assertThat(actual.getCity()).isEqualTo(expected.getCity());
        assertThat(actual.getPhone()).isEqualTo(expected.getPhone());
    }

    public final User USER_TEST = User.builder()//
    .id(generateUniqueId())//
    // .id(12L)//
    .lastName("Batcho")//
    .firstName("Karen Djayé")//
    .email("karen.test@live.fr")//
    .age(28)//
    .country("France")//
    .adresse("23 Rue du Commandant Mages")//
    .city("Marseille")//
    .phone("0645789512")//
    .build();
    
    public final User USER_TEST_ID = User.builder()//
    .id("83381733-d06a-4eb6-97c8-ee9eef873c9f")//
    // .id(12L)//
    .lastName("Batcho")//
    .firstName("Karen Djayé")//
    .email("karen.test@live.fr")//
    .age(28)//
    .country("France")//
    .adresse("23 Rue du Commandant Mages")//
    .city("Marseille")//
    .phone("0645789512")//
    .build();

    public final User USER_TEST_NO_ID = User.builder()//
    .lastName("Batcho")//
    .firstName("Karen Djayé")//
    .email("karen.test@live.fr")//
    .age(28)//
    .country("France")//
    .adresse("23 Rue du Commandant Mages")//
    .city("Marseille")//
    .phone("0645789512")//
    .build();

    public final User USER_AGE = User.builder()//
    .lastName("Adjiba")//
    .firstName("Naldine")//
    .email("naldine.test@live.fr")//
    .age(10)//
    .country("France")//
    .adresse("13 Rue Lakanal")//
    .city("Toulouse")//
    .phone("0645789512")//
    .build();
    
    public final User USER_SERVICE_TEST = User.builder()//
    .lastName("Adjiba")//
    .firstName("Naldine")//
    .email("naldine.test@live.fr")//
    .age(43)//
    .country("France")//
    .adresse("13 Rue Lakanal")//
    .city("Toulouse")//
    .phone("0645789512")//
    .build();
    
    
    /*
     * XXX: Attention les id sont auto-générés dans le SI
     * On force les traits au niveau Id pour pour pour pouvoir fair les recherches facilement pour les tests d'intégration
     */
    private static final String PAYS = "France";
    
    public static final User USER = User.builder()//
    .id("83381733-d06a-4eb6-97c8-ee9eef873c9f")//
    .lastName("Batcho")//
    .firstName("Karen Djayé")//
    .email("karen.test@live.fr")//
    .age(20)//
    .country(PAYS)//
    .adresse("23 Rue du Commandant Mages")//
    .city("Marseille")//
    .phone("0645789512")//
    .build();

    public static final  User USER2 = User.builder()//
    .id("83381733-d06a-4eb6-97c8-aa9cvef873c9f")//
    .lastName("Adjba")//
    .firstName("Italé Naldine")//
    .email("naldine.test@live.fr")//
    .age(28)//
    .country(PAYS)//
    .adresse("13 Rue Lakanal")//
    .city("Toulouse")//
    .phone("0645789912")//
    .build();

    public static final  User USER3 = User.builder()//
    .id("83381745-d08a-4eb6-97c8-ee9eef873c9f")//
    .lastName("Dessoum")//
    .firstName("André Marcel")//
    .email("marcel.test@live.fr")//
    .age(36)//
    .country(PAYS)//
    .adresse("1 Boulevard Wilson")//
    .city("Antibes")//
    .phone("0745788912")//
    .build();

    public static final  User USER4 = User.builder()//
    .id("89991733-d06a-4eb6-97c8-ee9eef873c9f")//
    .lastName("Belahy")//
    .firstName("Steeve")//
    .email("steeve.test@live.fr")//
    .age(32)//
    .country(PAYS)//
    .adresse("1 Boulevard Wilson")//
    .city("Juan Les Pins")//
    .phone("0759733912")//
    .build();
    
    public static final  User USER5 = User.builder()//
    .id("89995698-d06a-4eb6-97c8-ee9eef873c9f")//
    .lastName("Belahy update")//
    .firstName("Steeve update")//
    .email("steeve.test@live.fr")//
    .age(32)//
    .country(PAYS)//
    .adresse("1 Boulevard Wilson")//
    .city("Juan Les Pins")//
    .phone("0759733912")//
    .build();

    public static void assertAllUsers(final User expected, final User actual)
    {
        // assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
        assertThat(actual.getCountry()).isEqualTo(expected.getCountry());
        assertThat(actual.getAdresse()).isEqualTo(expected.getAdresse());
        assertThat(actual.getCity()).isEqualTo(expected.getCity());
        assertThat(actual.getPhone()).isEqualTo(expected.getPhone());
    }

    /**
     * Générer un identifiant unique UUID.
     * 
     * @return l'identifiant unique
     */
    public static String generateUniqueId()
    {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 
     * @return
     */
    public static  HttpHeaders httpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of the request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with
        return headers;
    }

}
