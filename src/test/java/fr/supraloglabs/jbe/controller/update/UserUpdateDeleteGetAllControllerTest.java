/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserUpdateDeleteGetAllControllerTest.java
 * Date de création : 21 févr. 2021
 * Heure de création : 03:57:22
 * Package : fr.supraloglabs.jbe.controller.update
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.controller.update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import fr.supraloglabs.jbe.TestsDataUtils;
import fr.supraloglabs.jbe.UserAccountApplicationStarter;
import fr.supraloglabs.jbe.config.AppRootConfig;
import fr.supraloglabs.jbe.model.dto.UserDTO;
import fr.supraloglabs.jbe.model.po.User;
import fr.supraloglabs.jbe.service.mapper.UserMapper;
import fr.supraloglabs.jbe.service.user.UserService;

/**
 * Classedes Tests d'intégration système des objets de type {@link UserUpdateDeleteGetAllController}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "userUpdateDeleteGetAllControllerTest", classes = { AppRootConfig.class, UserMapper.class, UserService.class,
        UserUpdateDeleteGetAllController.class })
@SpringBootTest(classes = UserAccountApplicationStarter.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserUpdateDeleteGetAllControllerTest
{
    //
    private static final String URL = "http://localhost:";// url du serveur REST. Cette url peut être celle d'un serveur distant.
    private static final String ALL_URI = "/api-users/user/users";
    private static final String ALL_URI_With_SORT = "/api-users/user/users?sort=lastName";
    private static final String UPDATE_URI = "/api-users/user/update/{id}";
    private static final String UPDATE_URI_BAD_VARIABLE = "/api-users/user/update/{identifiant}";
    private static final String UPDATE_URI_BAD = "/api-users/user/update/";
    private static final String DELETE_URI = "/api-users/user/destroy/{id}";
    private static final String DELETE_URI_BAD_VARIABLE = "/api-users/user/destroy/{identifiant}";
    private static final String DELETE_URI_BAD = "/api-users/user/destroy/";

    private static final String ID_PARAM = "id";

    //
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @LocalServerPort
    private int port; // permet d'utiliser le port local du serveur, sinon une erreur "Connexion refusée"

    private String getURLWithPort(String uri)
    {
        return URL + port + "/user-account" + uri;
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.controller.update.UserUpdateDeleteGetAllController#getUsersByPageable(org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testGetUsersByPageable()
    {
        final ResponseEntity<List<UserDTO>> allResponse = this.restTemplate.exchange(this.getURLWithPort(ALL_URI), HttpMethod.GET, null,
        new ParameterizedTypeReference<List<UserDTO>>()
        {
        });

        assertThat(allResponse).isNotNull();

        final List<UserDTO> body = (List<UserDTO>) allResponse.getBody();
        assertThat(body).isNotNull();

        assertThat(body.size()).isPositive();
        assertThat(body.size()).isEqualTo(4);
        assertThat(allResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testGetUsersByPageable_With_Sort()
    {
        final ResponseEntity<List<UserDTO>> allResponse = this.restTemplate.exchange(this.getURLWithPort(ALL_URI_With_SORT), HttpMethod.GET, null,
        new ParameterizedTypeReference<List<UserDTO>>()
        {
        });
        assertThat(allResponse).isNotNull();

        final List<UserDTO> body = (List<UserDTO>) allResponse.getBody();
        assertThat(body).isNotNull();

        assertThat(body.size()).isPositive();
        assertThat(body.size()).isEqualTo(4);
        assertThat(allResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testGetUsersByPageable_With_Bad_URL()
    {
        final String badUrl = this.getURLWithPort(ALL_URI) + "/api";

        final Exception exception = assertThrows(RestClientException.class, () -> {
            this.restTemplate.exchange(badUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDTO>>()
            {
            });
        });

        final String expectedMessage = "Error while extracting response for type";
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.controller.update.UserUpdateDeleteGetAllController#updateUser(java.lang.String, fr.supraloglabs.jbe.model.po.User)}.
     */
    @Order(1)
    @Test
    void testUpdateUser()
    {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of the request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        // "89991733-d06a-4eb6-97c8-ee9eef873c9f"
        final User user = TestsDataUtils.USER5;
        final User savedUser = this.userService.createUser(user);

        savedUser.setLastName("Nom maj");
        savedUser.setFirstName("Prenom maj");

        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, savedUser.getId());

        final UserDTO dto = this.userMapper.convertToUserDTO(user);
        final HttpEntity<UserDTO> entity = new HttpEntity<UserDTO>(dto, headers);

        final ResponseEntity<UserDTO> updateResponse = this.restTemplate.exchange(this.getURLWithPort(UPDATE_URI), HttpMethod.PUT, entity,
        UserDTO.class, variables);
        assertThat(updateResponse).isNotNull();

        // On supprime l'enregistrement
        this.userService.deleteUser(savedUser.getId());

        final UserDTO body = updateResponse.getBody();
        assertThat(body).isNotNull();
        assertThat(updateResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(updateResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        TestsDataUtils.assertAllUsersUserAndUserDTO(savedUser, body);
    }

    @Test
    void testUpdateUser_WitBad_Url()
    {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of the request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        final User user = TestsDataUtils.USER5;

        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, null);

        final HttpEntity<UserDTO> entity = new HttpEntity<UserDTO>(this.userMapper.convertToUserDTO(user));

        //
        final ResponseEntity<UserDTO> updateResponse = this.restTemplate.exchange(this.getURLWithPort(UPDATE_URI_BAD), HttpMethod.PUT, entity,
        UserDTO.class, variables);

        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testUpdateUser_WithNotExistId()
    {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of the request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        final User user = TestsDataUtils.USER5;

        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, "test");

        final HttpEntity<UserDTO> entity = new HttpEntity<UserDTO>(this.userMapper.convertToUserDTO(user));

        //
        final ResponseEntity<UserDTO> updateResponse = this.restTemplate.exchange(this.getURLWithPort(UPDATE_URI), HttpMethod.PUT, entity,
        UserDTO.class, variables);

        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testUpdateUser_WithBad_Variable()
    {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of the request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        final User user = TestsDataUtils.USER5;

        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, "test");

        final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            final HttpEntity<UserDTO> entity = new HttpEntity<UserDTO>(this.userMapper.convertToUserDTO(user));
            this.restTemplate.exchange(this.getURLWithPort(UPDATE_URI_BAD_VARIABLE), HttpMethod.PUT, entity, UserDTO.class, variables);
        });

        final String expectedMessage = "Map has no value for 'identifiant";
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testUpdateUser_WithNull()
    {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of the request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        final User user = TestsDataUtils.USER5;

        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, null);

        final HttpEntity<UserDTO> entity = new HttpEntity<UserDTO>(this.userMapper.convertToUserDTO(user));

        //
        final ResponseEntity<UserDTO> updateResponse = this.restTemplate.exchange(this.getURLWithPort(UPDATE_URI), HttpMethod.PUT, entity,
        UserDTO.class, variables);

        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.controller.update.UserUpdateDeleteGetAllController#deleteUser(java.lang.String)}.
     */
    @Test
    void testDeleteUser()
    {
        // "89991733-d06a-4eb6-97c8-ee9eef873c9f"
        final User user = TestsDataUtils.USER5;
        final User savedUser = this.userService.createUser(user);

        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, savedUser.getId());

        final ResponseEntity<String> responseEntity = this.restTemplate.exchange(this.getURLWithPort(DELETE_URI), HttpMethod.DELETE, null, String.class,
        variables);
        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testDeleteUser_With_NotExist_Id()
    {
        //
        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, "89991733-d06a-4eb6-97c8-ee9eef873c9f1455");

        final ResponseEntity<String> responseEntity = this.restTemplate.exchange(this.getURLWithPort(DELETE_URI), HttpMethod.DELETE, null, String.class,
        variables);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testDeleteUser_With_Bad_Variable()
    {
        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, "89991733-d06a-4eb6-97c8-ee9eef873c9f1455");

        final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.restTemplate.exchange(this.getURLWithPort(DELETE_URI_BAD_VARIABLE), HttpMethod.DELETE, null, Void.class, variables);
        });

        final String expectedMessage = "Map has no value for 'identifiant";
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testDeleteUser_With_Bad_Url()
    {
        final Map<String, String> variables = new HashMap<>(1);
        variables.put(ID_PARAM, "89991733-d06a-4eb6-97c8-ee9eef873c9f1455");

        final ResponseEntity<String> responseEntity = this.restTemplate.exchange(this.getURLWithPort(DELETE_URI_BAD), HttpMethod.DELETE, null,
        String.class, variables);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
