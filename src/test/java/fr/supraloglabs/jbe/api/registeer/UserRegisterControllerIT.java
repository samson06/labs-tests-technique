/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserRegisterControllerIT.java
 * Date de création : 18 févr. 2021
 * Heure de création : 19:32:48
 * Package : fr.supraloglabs.jbe.api.registeer
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.api.registeer;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.StringUtils;
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

import fr.supraloglabs.jbe.UserAccountApplicationStarter;
import fr.supraloglabs.jbe.config.AppRootConfig;
import fr.supraloglabs.jbe.model.dto.UserDTO;
import fr.supraloglabs.jbe.service.mapper.UserMapper;
import fr.supraloglabs.jbe.service.user.UserService;

/**
 * cLasse des Tests d'Intégration Système des objets de type {@link UserRegisterController}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "userRegisterControllerTest", classes = { AppRootConfig.class, UserMapper.class, UserService.class,
        UserRegisterController.class })
@SpringBootTest(classes = UserAccountApplicationStarter.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserRegisterControllerIT
{
    //
    private static final String URL = "http://localhost:";// url du serveur REST. Cette url peut être celle d'un serveur distant.
    private static final String API_URI = "/api-users/user/register";

    //
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port; // permet d'utiliser le port local du serveur, sinon une erreur "Connexion refusée"

    private String getURLWithPort(String uri)
    {
        return URL + port + "/user-account" + uri;
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.api.registeer.UserRegisterController#createNewUSerDetails(fr.supraloglabs.jbe.model.dto.UserDTO)}.
     */
    @Test
    void testCreateNewUSerDetails()
    {
        final UserDTO userMock = UserDTO.builder()//
        .lastName("test")//
        .firstName("test firstanme")//
        .email("test.test@live.fr")//
        .age(28)//
        .country("France")//
        .adresse("23 Rue du Commandant Mages")//
        .city("Marseille")//
        .phone("0645789512")//
        .build();

        final ResponseEntity<UserDTO> postResponse = restTemplate.postForEntity(this.getURLWithPort(API_URI), userMock, UserDTO.class);

        assertThat(postResponse).isNotNull();
        final UserDTO dto = postResponse.getBody();

        assertThat(dto).isNotNull();
    }

    @Test
    void testCreateNewUSerDetails_WithId()
    {
        final UserDTO userMock = UserDTO.builder()//
        .id("12L")//
        .lastName("test")//
        .firstName("test firstanme")//
        .email("test.test@live.fr")//
        .age(28)//
        .country("France")//
        .adresse("23 Rue du Commandant Mages")//
        .city("Marseille")//
        .phone("0645789512")//
        .build();

        final ResponseEntity<UserDTO> postResponse = restTemplate.postForEntity(this.getURLWithPort(API_URI), userMock, UserDTO.class);

        assertThat(postResponse).isNotNull();
        final UserDTO dto = postResponse.getBody();

        assertThat(dto).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(postResponse.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testCreateNewUSerDetails_WithExistMail()
    {
        final UserDTO userMock = UserDTO.builder()//
        .lastName("test")//
        .firstName("test firstanme")//
        .email("test.test@live.fr")//
        .age(28)//
        .country("France")//
        .adresse("23 Rue du Commandant Mages")//
        .city("Marseille")//
        .phone("0645789512")//
        .build();

        final ResponseEntity<UserDTO> postResponse = restTemplate.postForEntity(this.getURLWithPort(API_URI), userMock, UserDTO.class);

        assertThat(postResponse).isNotNull();
        final UserDTO dto = postResponse.getBody();

        assertThat(dto).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(postResponse.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testCreateNewUSerDetails_WithEmptyMail()
    {
        final UserDTO userMock = UserDTO.builder()//
        .lastName("test")//
        .firstName("test firstanme")//
        .email(StringUtils.EMPTY)//
        .age(28)//
        .country("France")//
        .adresse("23 Rue du Commandant Mages")//
        .city("Marseille")//
        .phone("0645789512")//
        .build();

        final ResponseEntity<UserDTO> postResponse = restTemplate.postForEntity(this.getURLWithPort(API_URI), userMock, UserDTO.class);

        assertThat(postResponse).isNotNull();
        final UserDTO dto = postResponse.getBody();

        assertThat(dto).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(postResponse.getStatusCodeValue()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void testCreateNewUSerDetails_WithNonValidUser()
    {
        final UserDTO userMock = UserDTO.builder()//
        .lastName("test")//
        .firstName("test firstanme")//
        .email("test12.test@live.fr")//
        .age(17)//
        .country("Italie")//
        .build();

        final ResponseEntity<UserDTO> postResponse = restTemplate.postForEntity(this.getURLWithPort(API_URI), userMock, UserDTO.class);

        assertThat(postResponse).isNotNull();
        final UserDTO dto = postResponse.getBody();

        assertThat(dto).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(postResponse.getStatusCodeValue()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void testCreateNewUSerDetails_WithEmptylUser()
    {
        final UserDTO userMock = UserDTO.builder()//
        .build();

        final ResponseEntity<UserDTO> postResponse = restTemplate.postForEntity(this.getURLWithPort(API_URI), userMock, UserDTO.class);

        assertThat(postResponse).isNotNull();
        final UserDTO dto = postResponse.getBody();

        assertThat(dto).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(postResponse.getStatusCodeValue()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void testCreateNewUSerDetails_WithNullUser()
    {
        final ResponseEntity<UserDTO> postResponse = restTemplate.postForEntity(this.getURLWithPort(API_URI), null, UserDTO.class);

        assertThat(postResponse).isNotNull();
        final UserDTO dto = postResponse.getBody();

        assertThat(dto).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(postResponse.getStatusCodeValue()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
