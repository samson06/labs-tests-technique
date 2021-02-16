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

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.supraloglabs.jbe.TestsDataUtils;
import fr.supraloglabs.jbe.model.GenericApiResponse;
import fr.supraloglabs.jbe.model.dto.UserDTO;
import fr.supraloglabs.jbe.model.dto.error.ResponseErrorDTO;
import fr.supraloglabs.jbe.model.dto.error.ValidationErrorDTO;

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
     * {@link fr.supraloglabs.jbe.util.UserAccountUtil#buildResponseErrorEntity(fr.supraloglabs.jbe.model.dto.error.ResponseErrorDTO)}.
     */
    @Test
    void testBuildResponseErrorEntity()
    {
        final ResponseErrorDTO responseError = ResponseErrorDTO.builder()//
        .status(HttpStatus.BAD_REQUEST) //
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(UserAccountUtil.HTTP_CLIENT_ERROR)//
        .debugMessage("Merreur mauvaise requête")//
        .validationErrors(null)//
        .build();

        GenericApiResponse<UserDTO> response = new GenericApiResponse<>();
        response.setData(TestsDataUtils.USER_DTO_TEST);
        response.setErrors(responseError);

        ResponseEntity<GenericApiResponse<UserDTO>> responseEntity = UserAccountUtil.buildResponseErrorEntity(responseError);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(responseEntity.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
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
     * Test method for
     * {@link fr.supraloglabs.jbe.util.UserAccountUtil#buildApiValidationError(java.lang.String, java.lang.String, java.lang.Object, java.lang.String)}.
     */
    @Test
    void testBuildApiValidationError()
    {
        final ValidationErrorDTO errorDTO = UserAccountUtil.buildApiValidationError(null, null, null, null);
        
        assertThat(errorDTO).isNotNull();
    }

}
