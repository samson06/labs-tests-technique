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

import java.util.UUID;

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
    //
    public final UserDTO USER_DTO_TEST = UserDTO.builder()//
    // .id(generateUniqueId())//
    .firstName("Batcho")//
    .lastName("Karen Djayé")//
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
    .firstName("Batcho")//
    .lastName("Karen Djayé")//
    .email("karen.test@live.fr")//
    .age(28)//
    .country("France")//
    .adresse("23 Rue du Commandant Mages")//
    .city("Marseille")//
    .phone("0645789512")//
    .build();

    public final User USER_TEST_NO_ID = User.builder()//
    // .id(generateUniqueId())//
    .firstName("Batcho")//
    .lastName("Karen Djayé")//
    .email("karen.test@live.fr")//
    .age(28)//
    .country("France")//
    .adresse("23 Rue du Commandant Mages")//
    .city("Marseille")//
    .phone("0645789512")//
    .build();

    public static void assertAllUsers(final User expected, final User actual)
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

    /**
     * Générer un identifiant unique UUID.
     * 
     * @return l'identifiant unique
     */
    public static String generateUniqueId()
    {
        return UUID.randomUUID().toString();
    }

}
