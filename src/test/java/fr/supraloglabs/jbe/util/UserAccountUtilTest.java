/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserAccountUtilTest.java
 * Date de création : 16 févr. 2021
 * Heure de création : 01:51:24
 * Package : fr.supraloglabs.jbe.util
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import fr.supraloglabs.jbe.error.AppCustomException;
import fr.supraloglabs.jbe.model.error.ErrorDetails;
import fr.supraloglabs.jbe.model.po.User;

/**
 * Classe des Tests Unitaires des objets de type {@link UserAccountUtil}
 * 
 * @author Vincent Otchoun
 */
class UserAccountUtilTest
{

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.util.UserAccountUtil#mongoDBConnectionStr(java.lang.String, java.lang.Integer, java.lang.String)}.
     */
    @Test
    void testMongoDBConectionStr()
    {
        String host = "OVIOK";
        Integer port = 12345;
        String base = "test_db";

        final String response = UserAccountUtil.mongoDBConnectionStr(host, port, base);

        assertThat(response).isEqualTo("mongodb://OVIOK:12345/test_db");
    }

    @Test
    void testMongoDBConectionStr_()
    {
        final String response = UserAccountUtil.mongoDBConnectionStr(null, null, null);

        assertThat(response).isNull();
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.util.UserAccountUtil#mongoDBConnectionStrDefault()}.
     */
    @Test
    void testMongoDBConnectionStrDefault()
    {
        final String response = UserAccountUtil.mongoDBConnectionStrDefault();

        assertThat(response).isNotNull();
        assertThat(response.toString()).isEqualToIgnoringCase("mongodb://localhost:12345/users_db_test");
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.util.UserAccountUtil#mongoDBConnectionWithDBName(java.lang.String)}.
     */
    @Test
    void testMongoDBConnectionWithDBName()
    {
        final String response = UserAccountUtil.mongoDBConnectionWithDBName("base_test");

        assertThat(response).isNotNull();
        assertThat(response.toString()).isEqualToIgnoringCase("mongodb://localhost:12345/base_test");
    }

    @Test
    void testMongoDBConnectionWithDBName_WithEmpty()
    {
        final String response = UserAccountUtil.mongoDBConnectionWithDBName(StringUtils.EMPTY);

        assertThat(response).isNull();
    }

    @Test
    void testMongoDBConnectionWithDBName_WithNull()
    {
        final String response = UserAccountUtil.mongoDBConnectionWithDBName(null);

        assertThat(response).isNull();
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.util.UserAccountUtil#buildResponseErrorEntity(fr.supraloglabs.jbe.model.error.ErrorDetails)}.
     */
    @Test
    void testBuildResponseErrorEntity()
    {
        final ErrorDetails responseError = ErrorDetails.builder()//
        .status(HttpStatus.BAD_REQUEST) //
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(UserAccountUtil.HTTP_CLIENT_ERROR)//
        .message("Merreur mauvaise requête")//
        .build();

        ResponseEntity<ErrorDetails> responseEntity = UserAccountUtil.buildResponseErrorEntity(responseError);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getBody()).isExactlyInstanceOf(ErrorDetails.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getDetails()).isEqualTo(UserAccountUtil.HTTP_CLIENT_ERROR);
    }

    @Test
    void testBuildResponseErrorEntity_WithNull()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            UserAccountUtil.buildResponseErrorEntity(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }
    
    /**
     * Test method for {@link fr.supraloglabs.jbe.util.UserAccountUtil#construireErreur(java.lang.Exception, org.springframework.web.context.request.WebRequest, org.springframework.http.HttpStatus)}.
     */
    @Test
    void testConstruireErreur()
    {
        final String message = "Message Erreur Exception globale";
        final Exception exception = new Exception(message);
        final WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(Boolean.TRUE)).thenReturn("Infos client : session id and user name");
        
        final ErrorDetails errorDetails = UserAccountUtil.construireErreur(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
        
        assertThat(errorDetails).isNotNull();
        assertThat(errorDetails.getMessage()).containsIgnoringCase("Erreur Exception globale");
        assertThat(errorDetails.getTimestamp()).isExactlyInstanceOf(LocalDateTime.class);
    }
    
    
    @Test
    void testConstruireErreur_WithCustom()
    {
        final String message = "Message Erreur Exception customisée";
        final AppCustomException exception = new AppCustomException(message);
        
        final WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(Boolean.TRUE)).thenReturn("Infos client customisées : session id and user name");
        
        final ErrorDetails errorDetails = UserAccountUtil.construireErreur(exception, webRequest, HttpStatus.NOT_FOUND);
        
        assertThat(errorDetails).isNotNull();
        assertThat(errorDetails.getMessage()).containsIgnoringCase("Erreur Exception customisée");
        assertThat(errorDetails.getTimestamp()).isExactlyInstanceOf(LocalDateTime.class);
        assertThat(errorDetails.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    
    @Test
    void testConstruireErreur_WithNull()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            UserAccountUtil.construireErreur(null, null, HttpStatus.NOT_FOUND);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }
    
    @Test
    void testConstruireErreur_WithFullNull()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            UserAccountUtil.construireErreur(null, null, null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }
    
    /**
     * Test method for {@link fr.supraloglabs.jbe.util.UserAccountUtil#isValidUser(fr.supraloglabs.jbe.model.po.User)}.
     */
    @Test
    void testIsValidUser()
    {
        final User user = User.builder()//
        .age(18)//
        .country("France")//
        .build();
        
        final Boolean response = UserAccountUtil.isValidUser(user);
        
        assertThat(response).isNotNull();
        assertThat(response.booleanValue()).isTrue();
    }
    
    @Test
    void testIsValidUser_ShouldReturnFalseWithAge()
    {
        final User user = User.builder()//
        .age(15)//
        .country("France")//
        .build();
        
        final Boolean response = UserAccountUtil.isValidUser(user);
        
        assertThat(response).isNotNull();
        assertThat(response.booleanValue()).isFalse();
    }
    
    @Test
    void testIsValidUser_ShouldReturnFalseWithCountry()
    {
        final User user = User.builder()//
        .age(20)//
        .country("Italie")//
        .build();
        
        final Boolean response = UserAccountUtil.isValidUser(user);
        
        assertThat(response).isNotNull();
        assertThat(response.booleanValue()).isFalse();
    }
    
    @Test
    void testIsValidUser_ShouldReturnFalseWithNull()
    {
        final Boolean response = UserAccountUtil.isValidUser(null);
        
        assertThat(response).isNotNull();
        assertThat(response.booleanValue()).isFalse();
    }
    
    /**
     * Test method for {@link fr.supraloglabs.jbe.util.UserAccountUtil#validUser(fr.supraloglabs.jbe.model.po.User)}.
     */
    @Test
    void testValidUser()
    {
        final User user = User.builder()//
        .age(18)//
        .country("France")//
        .build();
        UserAccountUtil.validUser(user);
        
        assertThat(user).isNotNull();
    }
    
    @Test
    void testValidUser_()
    {
        final User user = User.builder()//
        .age(20)//
        .country("Italie")//
        .build();
        
        assertThat(user).isNotNull();
    }
}
