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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import fr.supraloglabs.jbe.model.error.ErrorDetails;
import fr.supraloglabs.jbe.util.UserAccountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Composant de gestion des erreurs à remonter par les API en cas de dysfonctionnement de l'application.
 * 
 * @author Vincent Otchoun
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler<T>
{
    /**
     * Intercepter les exceptions levées lorsqu'une erreur HTTP Client avec le status (code HTTP) 4xx survient.
     * 
     * @param pException erreur survenue lors de l'appel client.
     * @param pRequest   les informations de la requête adressée.
     * @return entité de réponse HTTP avec les données sur les erreurs.
     */
    @ExceptionHandler(value = { HttpClientErrorException.class })
    public ResponseEntity<T> handleHttpClientErrorException(final HttpClientErrorException pException, final WebRequest pRequest)
    {
        log.info("[handleHttpClientErrorException] - Interception des erreurs de type 4XX.");

        // Construire l'objet portant les données des erreurs survenues
        final ErrorDetails responseError = ErrorDetails.builder()//
        .status(pException.getStatusCode()) //
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(pRequest.getDescription(false))//
        .message(pException.getMessage())//
        .build();
        return UserAccountUtil.buildResponseErrorEntity(responseError);
    }

    /**
     * Interception des exceptions personnalisées de l'application.
     * 
     * @param pException erreur applicative survenue.
     * @param pRequest   les informations de la requête adressée.
     * @return entité de réponse HTTP avec les données sur les erreurs.
     */
    @ExceptionHandler(value = { AppCustomException.class })
    public ResponseEntity<T> handleAppCustomException(final AppCustomException pException, final WebRequest pRequest)
    {
        log.info("[handleAppCustomException] - Interception des erreurs applicatives.");

        // Construire l'objet portant les données des erreurs survenues
        final ErrorDetails error = UserAccountUtil.construireErreur(pException, pRequest, HttpStatus.NOT_FOUND);
        return UserAccountUtil.buildResponseErrorEntity(error);
    }

    /**
     * Interception des autres type d'erreurs survenues lors de l'exécution de l'application.
     * 
     * @param ex       erreur interne survenue.
     * @param pRequest les informations de la requête adressée.
     * @return entité de réponse HTTP avec les données sur les erreurs.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<T> globleExcpetionHandler(final Exception pException, WebRequest pRequest)
    {
        log.info("[globleExcpetionHandler] - Interception des autres erreurs applicatives.");

        // Construire l'objet portant les données des erreurs survenues
        final ErrorDetails error = UserAccountUtil.construireErreur(pException, pRequest, HttpStatus.INTERNAL_SERVER_ERROR);
        return UserAccountUtil.buildResponseErrorEntity(error);
    }

}
