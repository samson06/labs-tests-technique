/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : GlobalExceptionHandlerTest.java
 * Date de création : 17 févr. 2021
 * Heure de création : 00:37:49
 * Package : fr.supraloglabs.jbe.error
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import fr.supraloglabs.jbe.TestsDataUtils;
import fr.supraloglabs.jbe.config.AppRootConfig;
import fr.supraloglabs.jbe.model.error.ErrorDetails;

/**
 * Classe des Tests Unitaires des objets de type {@link GlobalExceptionHandler}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "globalExceptionHandlerTest", classes = { AppRootConfig.class, GlobalExceptionHandler.class })
@SpringBootTest
@ActiveProfiles("test")
class GlobalExceptionHandlerTest
{
    @Autowired
    private GlobalExceptionHandler<ErrorDetails> exceptionHandler;

    @Mock
    private WebRequest webRequest;

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.error.GlobalExceptionHandler#handleHttpClientErrorException(org.springframework.web.client.HttpClientErrorException, org.springframework.web.context.request.WebRequest)}.
     */
    @Test
    void testHandleHttpClientErrorException()
    {
        final HttpClientErrorException clientErrorException = new HttpClientErrorException(HttpStatus.UNAUTHORIZED, TestsDataUtils.ACCESS_DENIED);
        final ResponseEntity<ErrorDetails> response = this.exceptionHandler.handleHttpClientErrorException(clientErrorException, this.webRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("401 Accès non autorisés.");
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getDetails()).isNull();
    }
    
    @Test
    void testHandleHttpClientErrorException_BadRequest()
    {
        final HttpClientErrorException clientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST, TestsDataUtils.ACCESS_DENIED);
        final ResponseEntity<ErrorDetails> response = this.exceptionHandler.handleHttpClientErrorException(clientErrorException, this.webRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("400 Accès non autorisés.");
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getDetails()).isNull();
    }
    
    @Test
    void testHandleHttpClientErrorException_ShouldThrowException()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            new HttpClientErrorException(null, null);
        });

        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.error.GlobalExceptionHandler#handleAppCustomException(fr.supraloglabs.jbe.error.AppCustomException, org.springframework.web.context.request.WebRequest)}.
     */
    @Test
    void testHandleAppCustomException()
    {
        final AppCustomException customException = new AppCustomException("Erreur customisée");
        final ResponseEntity<ErrorDetails> response = this.exceptionHandler.handleAppCustomException(customException, this.webRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(customException.getMessage());
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getDetails()).isNull();
    }
    
    
    @Test
    void testHandleAppCustomException_WithNull()
    {
        final AppCustomException customException = new AppCustomException(null,null);
        final ResponseEntity<ErrorDetails> response = this.exceptionHandler.handleAppCustomException(customException, this.webRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isNull();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getDetails()).isNull();
    }
    
    @Test
    void testHandleAppCustomException_ShouldTrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleAppCustomException(null,null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.error.GlobalExceptionHandler#globleExcpetionHandler(java.lang.Exception, org.springframework.web.context.request.WebRequest)}.
     */
    @Test
    void testGlobleExcpetionHandler()
    {
        final Exception customException = new Exception("Erreur générale");
        final ResponseEntity<ErrorDetails> response = this.exceptionHandler.globleExcpetionHandler(customException, this.webRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(customException.getMessage());
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getDetails()).isNull();
    }
    
    @Test
    void testGlobleExcpetionHandler_WithNull()
    {
        final Exception exception = new Exception(null,null);
        final ResponseEntity<ErrorDetails> response = this.exceptionHandler.globleExcpetionHandler(exception, this.webRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isNull();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getDetails()).isNull();
    }
    
    @Test
    void testGlobleExcpetionHandler_ShouldTrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.globleExcpetionHandler(null,null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }
}
