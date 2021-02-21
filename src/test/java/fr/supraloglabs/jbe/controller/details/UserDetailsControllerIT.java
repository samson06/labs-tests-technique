/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserDetailsControllerIT.java
 * Date de création : 19 févr. 2021
 * Heure de création : 11:30:33
 * Package : fr.supraloglabs.jbe.controller.details
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.controller.details;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import fr.supraloglabs.jbe.TestsDataUtils;
import fr.supraloglabs.jbe.UserAccountApplicationStarter;
import fr.supraloglabs.jbe.config.AppRootConfig;
import fr.supraloglabs.jbe.controller.register.UserRegisterController;
import fr.supraloglabs.jbe.model.dto.UserDTO;
import fr.supraloglabs.jbe.model.po.User;
import fr.supraloglabs.jbe.service.mapper.UserMapper;
import fr.supraloglabs.jbe.service.user.UserService;

/**
 * Classe des Tests d'Intégration systèmes des objets de type {@link UserDetailsController}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "userDetailsControllerIT", classes = { AppRootConfig.class, UserMapper.class, UserService.class,
        UserRegisterController.class })
@SpringBootTest(classes = UserAccountApplicationStarter.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserDetailsControllerIT
{
    //
    private static final String URL = "http://localhost:";// url du serveur REST. Cette url peut être celle d'un serveur distant.
    private static final String API_URL = "/api-users/user/search/{id}";
    private static final String API_URL_QUERY_PARAM = "/api-users/user/search/{id}?country=Italie";
    private static final String ID_PARAM = "id";

    @LocalServerPort
    private int port; // permet d'utiliser le port local du serveur, sinon une erreur "Connexion refusée"
    //
    @Autowired
    private TestRestTemplate restTemplate;

    private String getURLWithPort(String uri)
    {
        return URL + port + "/user-account" + uri;
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.controller.details.UserDetailsController#getUSerDetails(java.lang.String, java.lang.String)}.
     */
    @Test
    void testGetUSerDetails()
    {
        final User searchedUser = TestsDataUtils.USER;

        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, searchedUser.getId());

        final ResponseEntity<UserDTO> getResponse = this.restTemplate.getForEntity(this.getURLWithPort(API_URL), UserDTO.class, variables);

        assertThat(getResponse).isNotNull();
        final UserDTO body = getResponse.getBody();

        assertThat(body).isNotNull();
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        TestsDataUtils.assertAllUsersUserAndUserDTO(searchedUser, body);
    }

    @Test
    void testGetUSerDetails_With_Bad_Id()
    {
        final User searchedUser = TestsDataUtils.USER;

        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, searchedUser.getId() + "4587");

        final ResponseEntity<UserDTO> getResponse = this.restTemplate.getForEntity(this.getURLWithPort(API_URL), UserDTO.class, variables);
        assertThat(getResponse).isNotNull();

        final UserDTO body = getResponse.getBody();
        assertThat(body).isNotNull();
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResponse.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testGetUSerDetails_With_Null_Id()
    {
        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, null);

        final ResponseEntity<UserDTO> getResponse = this.restTemplate.getForEntity(this.getURLWithPort(API_URL), UserDTO.class, variables);

        assertThat(getResponse).isNotNull();
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResponse.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testGetUSerDetails_With_Bad_Country()
    {
        final User searchedUser = TestsDataUtils.USER;

        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, searchedUser.getId());

        final ResponseEntity<UserDTO> getResponse = this.restTemplate.getForEntity(this.getURLWithPort(API_URL_QUERY_PARAM), UserDTO.class,
        variables);

        assertThat(getResponse).isNotNull();
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResponse.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
