/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserAccountUtil.java
 * Date de création : 15 févr. 2021
 * Heure de création : 01:56:43
 * Package : fr.supraloglabs.jbe.util
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.function.BiFunction;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.google.common.collect.Sets;

import fr.supraloglabs.jbe.error.AppCustomException;
import fr.supraloglabs.jbe.model.error.ErrorDetails;
import fr.supraloglabs.jbe.model.po.User;
import lombok.experimental.UtilityClass;

/**
 * Utilitaire de l'application.
 * 
 * @author Vincent Otchoun
 */
@UtilityClass
public class UserAccountUtil
{

    public static final String ENV = "prod";
    
    // Controller - SWAGGER
    public static final String ALREADY_EXIST_USER_ID_MSG = "Un nouvel utilisateur ne peut pas déjà avoir un identifiant.";
    public static final String ALREADY_EXIST_USER_EMAIL_MSG = "Conflit: l'email existe déjà dans la base de données.";
    public static final String USER_DATE_SUCCES_MSG = "Les données utilisateur ont été enregistrées avec succès.";
    public static final String USER_DATA_NOT_NULL_MSG = "Impossible d'enregistrer un utilsateur sans données dans le système d'informations.";
    public static final String USER_ID_NOT_NULL_MSG = "Impossible de trouver les détails de l'utilisateur : ";
    public static final String USER_ID_SEARCH_MSG = "L'utilisateur recherché : [";
    public static final String USER_ID_SEARCH_END_MSG = "], n'existe pas dans le système d'informations.";
    public static final String ALL_USERS_EMPTY = "Il n'y a pas d'utilisateurs enregistrés";
    public static final String DELETE_OK_MSG = "Suppression des données de l'utilisateur effectué avec succès";

    public static final String USER_ID_MSG_PATTERN = "%s%s%s";

    // "mongodb://localhost:12345/users_db_test"
    public static final String MONGODB_PREFIX_PATTERN = "mongodb://%s:%d/%s";

    private static final String MONGO_DB_URL = "localhost";
    private static final Integer MONGO_DB_PORT = 12345;
    private static final String MONGO_DB = "users_db_test";

    // Erreur HTTP
    public static final String HTTP_CLIENT_ERROR = "Erreur client HTTP.";

    // DAO messages
    public static final String SAVE_MSG = "Erreur Sauvegarde en base de donnnées des détails du nouvel utilisateur.";
    public static final String FIND_BY_EMAIL_MSG = "Erreur lors de la recherche des détails de l'utilisteur par son email.";
    public static final String FIND_BY_ID_MSG = "Erreur lors de la recherche des détails de l'utilisteur par son identifiant.";
    public static final String FIND_BY_ID_WITH_NAMES_MSG = "Erreur lors de la recherche des détails de l'utilisteur avec son identifiant par nom et prénom.";

    //
    public static final String CET_DATE_FORMAT_WITHOUT_TIMEZONE_TEXT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String CET_TIMEZONE = "Europe/Paris";
    public static final String FR_LOCALE = "fr-FR";
    public static final String US_LOCALE = "en-US";

    // Constante de validation des attributs de l'utilisateur
    public static final String PRENOM_MSG = "Le prénom ne doit pas être null ou vide.";
    public static final String PRENOM_SIZE_MSG = "Le prénom doit contenir au plus 40 caracètre.";
    public static final String NOM_MSG = "Le nom ne doit pas être null ou vide.";
    public static final String NOM_SIZE_MSG = "Le nom doit contenir au plus 50 caracètre.";
    public static final String EMAIL_MSG = "L'adresse mail ne doit pas être null ou vide.";
    public static final String EMAIL_SIZE_MSG = "L'adresse mail doit contenir au plus 50 caractères.";
    public static final String AGE_MSG = "L'age est obligatoire.";
    public static final String PAYS_MSG = "Le pays de résidence est obligatoire. Le pays autorisé pour la création de compte dans le système est la France.";
    public static final String PAYS_SIZE_MSG = "Le pays doit contenir au plus 50 caractères.";
    public static final String AGE_MIN_MSG = "L'âge minimum de création de compte dans le système est de 18 ans.";
    public static final String PAYS_REF = "FRANCE";
    public static final String MAJEUR_FR_USER_MSG = "Vous n'êtes pas autorisé à créér un compte dans le système. Il faut avoir au moins 18 ans et vivre en France";

    public static final int CINQUANTE = 50;
    public static final int QUATRE_VINGT = 80;

    // SWAGGER
    public static final String AUTH_BAERER_TOKEN = "Bearer %token";
    public static final String AUTHORISATION = "Authorization";
    public static final String HEADER = "Header";

    public static final String DEFAULT_GROUPENAME = "Page de Garde";
    public static final String USER_DETAILS_GROUPENAME = "Détails Utilisateur";
    public static final String REGISTER_USER_GROUPENAME = "Créer Utilisateur";
    public static final String AUTRES_USER_OPERATIONS_GROUPENAME = "Nouvelles Fonctionnalités";

    public static final String DEFAULT_CONTROLLER_BASE_PACKAGE = "fr.supraloglabs.jbe.controller";
    public static final String USER_DETAILS_CONTROLLER_BASE_PACKAGE = "fr.supraloglabs.jbe.controller.details";
    public static final String REGISTER_USER_CONTROLLER_BASE_PACKAGE = "fr.supraloglabs.jbe.controller.register";
    public static final String AUTRES_USER_OPERATIONS_CONTROLLER_BASE_PACKAGE = "fr.supraloglabs.jbe.controller.update";

    public static final Set<String> MEDIA_SET = Sets.newHashSet();
    static
    {
        MEDIA_SET.add(MediaType.APPLICATION_JSON_VALUE);
    }

    public static final String TITLE = "API's Gestion des Comptes Utilisateurs";
    public static final String DESCRITPION = "<b><font color='#e63f1b'>Gestion des données utilisateurs dans le système d'informations.</font></b>";
    public static final String VERSION = "3.0";
    public static final String TERME_OF_SERVICE_URL = "1.0.0";
    public static final String CONTACT_NAME = "Vincent OTCHOUN";
    public static final String CONTACT_URL = "www.tuto.vincent.fr";
    public static final String CONTACT_MAIL = "vincokri@live.fr";
    public static final String LICENCE = "Apache 2.0";
    public static final String LICENCE_URL = "http://www.apache.org/licenses/LICENSE-2.0.html";

    public static final String DEFAULT_INCLUDE_PATTERN = "1.0.0";
    public static final String HOST = "host1";
    public static final String DEFAULT_RESPONSE_MESSAGE = "false";
    public static final String CODE_GENERATION = "false";
    public static final String BASE_PACKAGE = "fr.supraloglabs.jbe.controller";

    // Fonction
    public static final BiFunction<String, String, Boolean> IGNORE_CASE = String::equalsIgnoreCase;

    /**
     * Construire l'objet d'encapsulation des des données sur les erreurs.
     * 
     * @param pException  erreur survenue.
     * @param pRequest    les information de la requête adressée.
     * @param pHttpStatus le code statut HTTP correspondant.
     * @return l'objet encapsulant les données sur les erreurs.
     */
    public static ErrorDetails construireErreur(final Exception pException, final WebRequest pRequest, final HttpStatus pHttpStatus)
    {
        return ErrorDetails.builder()//
        .status(pHttpStatus)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(pRequest.getDescription(Boolean.FALSE.booleanValue()))// inclure les infos clients tels que : session id et user name
        .message(pException.getMessage())//
        .build();
    }

    /**
     * Construire la réponse HTTP avec le status et le corps.
     * 
     * @param pResponseErrorDTO entité de gestion des erreurs pour la réponse.
     * @return objet encapsulant la réponse avec le statut HTTP.
     */
    @SuppressWarnings("unchecked")
    public static <T> ResponseEntity<T> buildResponseErrorEntity(final ErrorDetails pResponseErrorDTO)
    {
        return new ResponseEntity<>((T) pResponseErrorDTO, pResponseErrorDTO.getStatus());
    }

    /**
     * Fournit la chaîne de connexion à la base de données avec les paramètres fournis en paramètres.
     * 
     * @param pHostname le nom de l'hôte du serveur.
     * @param pPort     le port de connexion du serveur.
     * @param pDatabase le nom de la base de données.
     * @return la chaîne de connexion à la base de données.
     */
    public static String mongoDBConnectionStr(final String pHostname, final Integer pPort, final String pDatabase)
    {
        return (StringUtils.isNotBlank(pHostname) && pPort != null && StringUtils.isNotBlank(pDatabase)) ? String.format(MONGODB_PREFIX_PATTERN,
        pHostname, pPort, pDatabase) : null;
    }

    /**
     * Construire la chaîne avec les valeurs par défaut.
     * 
     * @return la chaîne de connexion à la base de données.
     */
    public static String mongoDBConnectionStrDefault()
    {
        return mongoDBConnectionStr(MONGO_DB_URL, MONGO_DB_PORT, MONGO_DB);
    }

    /**
     * Construire la chaîne de connexion à la base de données avec le nom de la base en paramètres.
     * 
     * @param pDatabasename le nom de la base de donées.
     * @return la chaîne de connexion à la base de données.
     */
    public static String mongoDBConnectionWithDBName(final String pDatabasename)
    {
        return mongoDBConnectionStr(MONGO_DB_URL, MONGO_DB_PORT, pDatabasename);
    }

    /**
     * Vérifier que l'utilisateur est autorisé à créer un compte dans le système d'informations.
     * 
     * @param pUser les données de l'utilisateur à sauvegarder en base de données.
     * @return
     */
    public static final Boolean isValidUser(final User pUser)
    {
        return pUser != null && (pUser.getAge() != null && pUser.getAge() >= 18) && StringUtils.isNotBlank(pUser.getCountry()) && IGNORE_CASE.apply(
        PAYS_REF, pUser.getCountry());
    }

    /**
     * Contruire le message d'interdiction de création de compte dans le système d'informations.
     * 
     * @param pUser les données de l'utilisateur à sauvegarder en base de données.
     */
    public static void validUser(final User pUser)
    {
        final boolean isMajeurFrance = isValidUser(pUser);
        if (!isMajeurFrance)
        {
            throw new AppCustomException(MAJEUR_FR_USER_MSG);
        }
    }

    /**
     * Construire le message d'erreur pour recherche infructueuse sur l'identifnant de l'utilisateur.
     * 
     * @param userId identifiant de l'utilisateur.
     * @return le message d'erreurs.
     */
    public static String buildMessage(final String userId)
    {
        return String.format(UserAccountUtil.USER_ID_MSG_PATTERN, UserAccountUtil.USER_ID_SEARCH_MSG, userId, UserAccountUtil.USER_ID_SEARCH_END_MSG);
    }

    /**
     * Intercepter l'exception surnue.
     * 
     * @param userId identifiant de l'utilisateur.
     */
    public static void buildExcemption(final String userId)
    {
        throw new AppCustomException(buildMessage(userId));
    }

}
