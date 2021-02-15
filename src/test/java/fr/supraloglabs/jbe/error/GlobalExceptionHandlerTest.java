/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : GlobalExceptionHandlerTest.java
 * Date de création : 15 févr. 2021
 * Heure de création : 10:58:49
 * Package : fr.supraloglabs.jbe.error
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.collect.Sets;

import fr.supraloglabs.jbe.config.AppRootConfig;
import fr.supraloglabs.jbe.model.GenericApiResponse;
import fr.supraloglabs.jbe.model.dto.UserDTO;
import fr.supraloglabs.jbe.model.dto.error.ResponseErrorDTO;
import fr.supraloglabs.jbe.util.UserAccountUtil;

/**
 * Classe des Tests Unitaires des objets de type {@link GlobalExceptionHandler}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:application-test.properties" })
@ContextConfiguration(name = "globalExceptionHandlerTest", classes = { AppRootConfig.class, GlobalExceptionHandler.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @SpringBootTest
@ActiveProfiles("test")
public class GlobalExceptionHandlerTest
{
    @Autowired
    private GlobalExceptionHandler<UserDTO> exceptionHandler;

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.exceptionHandler = null;
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.error.GlobalExceptionHandler#handleHttpClientErrorException(org.springframework.web.client.HttpClientErrorException)}.
     */
    @Test
    public void testHandleHttpClientErrorException()
    {
        final HttpClientErrorException clientErrorException = new HttpClientErrorException(HttpStatus.UNAUTHORIZED, UserAccountUtil.ACCESS_DENIED);
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleHttpClientErrorException(clientErrorException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(UserAccountUtil.HTTP_CLIENT_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(clientErrorException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleHttpClientErrorException_BadRequest()
    {
        final HttpClientErrorException clientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST, UserAccountUtil.ACCESS_DENIED);
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleHttpClientErrorException(clientErrorException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(UserAccountUtil.HTTP_CLIENT_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(clientErrorException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleHttpClientErrorException_ShouldThrowException()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            new HttpClientErrorException(null, null);
        });

        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.error.GlobalExceptionHandler#handleHttpUserAccountException(fr.supraloglabs.jbe.error.UserAccountException)}.
     */
    @Test
    public void testHandleHttpUserAccountException()
    {
        final UserAccountException customAppException = new UserAccountException("Erreur customisée");
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleHttpUserAccountException(customAppException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(UserAccountUtil.SERVER_INTERNAL_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(customAppException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleHttpUserAccountException_WithNull()
    {
        final UserAccountException customAppException = new UserAccountException(null, null);
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleHttpUserAccountException(customAppException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(UserAccountUtil.SERVER_INTERNAL_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(customAppException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleHttpUserAccountException_ShouldTrowNP()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleHttpUserAccountException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.error.GlobalExceptionHandler#handleNotReadableException(java.lang.Exception)}.
     */
    @Test
    public void testHandleNotReadableException()
    {
        MockHttpInputMessage inputMessage = new MockHttpInputMessage("mockInput".getBytes());
        final HttpMessageNotReadableException notReadableException = new HttpMessageNotReadableException("HttpMessageNotReadableExceptione", null,
        inputMessage);
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleNotReadableException(notReadableException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(UserAccountUtil.FORMAT_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(notReadableException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleNotReadableException_Json()
    {
        final JsonParseException jsonParseException = new JsonParseException(null, "JSON");
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleNotReadableException(jsonParseException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(UserAccountUtil.FORMAT_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(jsonParseException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleNotReadableException_WithNull()
    {
        final HttpMessageNotReadableException notReadableException = new HttpMessageNotReadableException(null, null, null);
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleNotReadableException(notReadableException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(UserAccountUtil.FORMAT_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(notReadableException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleNotReadableException_ShouldTrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleNotReadableException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.error.GlobalExceptionHandler#handleNoHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException)}.
     */
    @Test
    public void testHandleNoHandlerFoundException()
    {
        ServletServerHttpRequest req = new ServletServerHttpRequest(new MockHttpServletRequest("GET", "/resource"));
        final NoHandlerFoundException noHandlerFoundException = new NoHandlerFoundException(req.getMethod().toString(), req.getServletRequest()
        .getRequestURI(), req.getHeaders());
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleNoHandlerFoundException(noHandlerFoundException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isNotEqualTo(UserAccountUtil.URL_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(noHandlerFoundException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleNoHandlerFoundExceptionWithNull()
    {
        final NoHandlerFoundException noHandlerFoundException = new NoHandlerFoundException(null, null, null);
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleNoHandlerFoundException(noHandlerFoundException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isNotEqualTo(UserAccountUtil.URL_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(noHandlerFoundException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleNoHandlerFoundException_ShouldThrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleNoHandlerFoundException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.error.GlobalExceptionHandler#handleFileldValidationError(org.springframework.web.bind.MethodArgumentNotValidException)}.
     */
    @Test
    public void testHandleFileldValidationError()
    {
        final MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        final BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(new FieldError("station", "name", "should not be empty")));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleFileldValidationError(
        methodArgumentNotValidException);

        // assertThat(response, notNullValue());
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(UserAccountUtil.CONTRAINST_VALDATION_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(methodArgumentNotValidException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNotEmpty();
    }

    @Test
    public void testHandleFileldValidationError_WithNull()
    {
        final MethodArgumentNotValidException methodArgumentNotValidException = new MethodArgumentNotValidException(null, null);
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleFileldValidationError(methodArgumentNotValidException);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    @Test
    public void testHandleFileldValidationError_ShouldThrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleFileldValidationError(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.error.GlobalExceptionHandler#handleConstraintViolationException(javax.validation.ConstraintViolationException)}.
     */
    @Test
    public void testHandleConstraintViolationException()
    {
        Set<ConstraintViolation<UserDTO>> constraintViolations = Sets.newHashSet();
        final ConstraintViolationException violationException = new ConstraintViolationException("Violation de contraintes", constraintViolations);
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleConstraintViolationException(violationException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(UserAccountUtil.CONTRAINST_VALDATION_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(violationException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleConstraintViolationException_WithNull()
    {
        final ConstraintViolationException violationException = new ConstraintViolationException(null, null);
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleConstraintViolationException(violationException);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    @Test
    public void testHandleConstraintViolationException_ShouldThrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleConstraintViolationException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.error.GlobalExceptionHandler#handleDataIntegrityException(org.springframework.dao.DataIntegrityViolationException)}.
     */
    @Test
    public void testHandleDataIntegrityException()
    {
        final DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException(
        "Violation intégrité des données.");
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleDataIntegrityException(
        dataIntegrityViolationException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(UserAccountUtil.INTEGRITY_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(dataIntegrityViolationException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleDataIntegrityException_WithNull()
    {
        final DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException(null, null);
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleDataIntegrityException(
        dataIntegrityViolationException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(UserAccountUtil.INTEGRITY_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(dataIntegrityViolationException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleDataIntegrityException_ShouldThrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleDataIntegrityException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.error.GlobalExceptionHandler#handleMethodArgumentTypException(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException)}.
     * 
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testHandleMethodArgumentTypException() throws NoSuchMethodException, SecurityException
    {
        Method method = UserDTO.class.getMethod("setFirstName", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentTypeMismatchException mismatchException = new MethodArgumentTypeMismatchException(null, UserDTO.class, "name", methodParameter,
        null);
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleMethodArgumentTypException(mismatchException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isNotEqualTo(UserAccountUtil.METHOD_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(mismatchException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleMethodArgumentTypException_WithNull()
    {
        MethodArgumentTypeMismatchException mismatchException = new MethodArgumentTypeMismatchException(null, null, null, null, null);
        final ResponseEntity<GenericApiResponse<UserDTO>> response = this.exceptionHandler.handleMethodArgumentTypException(mismatchException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ResponseErrorDTO.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isNotEqualTo(UserAccountUtil.METHOD_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(mismatchException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    public void testHandleMethodArgumentTypException_ShouldThrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleMethodArgumentTypException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }
}
