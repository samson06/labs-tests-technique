/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : GlobalExceptionHandler.java
 * Date de création : 15 févr. 2021
 * Heure de création : 10:26:08
 * Package : fr.supraloglabs.jbe.error
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.error;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonParseException;

import fr.supraloglabs.jbe.model.GenericApiResponse;
import fr.supraloglabs.jbe.model.dto.error.ResponseErrorDTO;
import fr.supraloglabs.jbe.util.UserAccountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Composant de gestion des erreurs à remonter par les API en cas de dysfonctionnement.
 * 
 * @author Vincent Otchoun
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler<T>
{
    /**
     * Interception des exceptions levées lorsqu'une erreur ayant le status (code HTTP) 4xx survient.
     * 
     * @param ex erreur survenue.
     * @return entité de réponse HTTP avec le status et le corps.
     */
    @ExceptionHandler(value = { HttpClientErrorException.class })
    public ResponseEntity<GenericApiResponse<T>> handleHttpClientErrorException(HttpClientErrorException ex)
    {
        log.info("[handleHttpClientErrorException] - Interception des erreurs de type 4XX.");

        // Construire l'objet de restitution des informations sur les erreurs.
        final ResponseErrorDTO responseError = ResponseErrorDTO.builder()//
        .status(ex.getStatusCode()) //
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(UserAccountUtil.HTTP_CLIENT_ERROR)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null)//
        .build();
        return UserAccountUtil.buildResponseErrorEntity(responseError);
    }

    /**
     * Interception des exceptions internes propres au server (erreurs internes levées).
     * 
     * @param ex erreur interne survenue.
     * @return entité de réponse HTTP avec le status et le corps.
     */
    @ExceptionHandler(value = { UserAccountException.class })
    public ResponseEntity<GenericApiResponse<T>> handleHttpUserAccountException(UserAccountException ex)
    {
        log.info("[handleHttpCustomAppException] - Interception des erreurs internes à l'application.");

        final ResponseErrorDTO error = ResponseErrorDTO.builder()//
        .status(HttpStatus.INTERNAL_SERVER_ERROR)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(UserAccountUtil.SERVER_INTERNAL_ERROR)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null)//
        .build();
        return UserAccountUtil.buildResponseErrorEntity(error);
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class, JsonParseException.class, HttpMessageNotWritableException.class })
    public ResponseEntity<GenericApiResponse<T>> handleNotReadableException(Exception ex)
    {
        log.info("[handleNotReadableException] - Interception des erreurs de structure mal formatée.");

        final ResponseErrorDTO error = ResponseErrorDTO.builder()//
        .status(HttpStatus.UNPROCESSABLE_ENTITY)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(UserAccountUtil.FORMAT_ERROR)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null).build();
        return UserAccountUtil.buildResponseErrorEntity(error);
    }

    /**
     * Interception des erreurs d'URL non valide.
     * 
     * @param ex erreur ou exception levée.
     * @return entité de réponse HTTP avec le status et le corps.
     */
    @ExceptionHandler(value = { NoHandlerFoundException.class })
    public ResponseEntity<GenericApiResponse<T>> handleNoHandlerFoundException(NoHandlerFoundException ex)
    {
        log.info("[handleNoHandlerFoundException] - Interception des erreurs d'URL non valide.");

        final ResponseErrorDTO error = ResponseErrorDTO.builder()//
        .status(HttpStatus.BAD_REQUEST)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(String.format(UserAccountUtil.URL_ERROR, ex.getHttpMethod(), ex.getRequestURL()))//
        .debugMessage(ex.getMessage())//
        .validationErrors(null).build();
        return UserAccountUtil.buildResponseErrorEntity(error);
    }

    /**
     * Intercèpter les erreurs de cahmps ou attributs lorsque @Valid échoue.
     * 
     * @param ex erreur ou exception levée.
     * @return entité de réponse HTTP avec le status et le corps
     */
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<GenericApiResponse<T>> handleFileldValidationError(MethodArgumentNotValidException ex)
    {
        log.info("[handleConstraintViolationException] - Interception des erreurs de violation d contraintes.");

        final ResponseErrorDTO error = ResponseErrorDTO.builder()//
        .status(HttpStatus.BAD_REQUEST)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(UserAccountUtil.CONTRAINST_VALDATION_ERROR)//
        .debugMessage(ex.getMessage()).build();

        final BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        error.addFieldErrorValidationErrors(fieldErrors);
        return UserAccountUtil.buildResponseErrorEntity(error);
    }
    
    /**
     * Intercèpter les erreurs de violations de contraintes lorsque @Validated échoue.
     * 
     * @param ex erreur ou exception levée.
     * @return entité de réponse HTTP avec le status et le corps.
     */
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<GenericApiResponse<T>> handleConstraintViolationException(ConstraintViolationException ex)
    {
        log.info("[handleConstraintViolationException] - Interception des erreurs de violation d contraintes.");

        final ResponseErrorDTO error = ResponseErrorDTO.builder()//
        .status(HttpStatus.BAD_REQUEST)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(UserAccountUtil.CONTRAINST_VALDATION_ERROR)//
        .debugMessage(ex.getMessage()).build();

        error.addValidationErrorsCV(ex.getConstraintViolations());
        return UserAccountUtil.buildResponseErrorEntity(error);
    }

    /**
     * Intercepter les erreurs de violations d'intégrités des données.
     * 
     * @param ex erreur ou exception levée.
     * @return entité de réponse HTTP avec le status et le corps.
     */
    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    public ResponseEntity<GenericApiResponse<T>> handleDataIntegrityException(DataIntegrityViolationException ex)
    {
        log.info("[handleDataIntegrityException] - Interception des erreurs pour violation d'intégrité des données.");

        final ResponseErrorDTO error = ResponseErrorDTO.builder()//
        .status(HttpStatus.CONFLICT)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(UserAccountUtil.INTEGRITY_ERROR)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null).build();
        return UserAccountUtil.buildResponseErrorEntity(error);
    }
    
    /**
     * Intercepter les erreurs de non-concordance de type d'argument de méthode.
     * 
     * @param ex erreur ou exception levée.
     * @return entité de réponse HTTP avec le status et le corps.
     */
    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
    public ResponseEntity<GenericApiResponse<T>> handleMethodArgumentTypException(MethodArgumentTypeMismatchException ex)
    {
        log.info("[handleMethodArgumentTypException] - Interception des erreurs de non-concordance de type d'argument de  méthode.");

        final String details = String.format(UserAccountUtil.METHOD_ERROR, ex.getName(), ex.getValue(), ex.getRequiredType()+StringUtils.EMPTY);
        final ResponseErrorDTO error = ResponseErrorDTO.builder()//
        .status(HttpStatus.BAD_REQUEST)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(details)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null).build();
        return UserAccountUtil.buildResponseErrorEntity(error);
    }
}
