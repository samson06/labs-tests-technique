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

import org.springframework.http.ResponseEntity;

import fr.supraloglabs.jbe.model.GenericApiResponse;
import fr.supraloglabs.jbe.model.dto.error.ResponseErrorDTO;
import lombok.experimental.UtilityClass;

/**
 * Utilitaire de l'application.
 * 
 * @author Vincent Otchoun
 */
@UtilityClass
public class UserAccountUtil
{
    // Erreur HTTP
    public static final String URL_ERROR = "Impossible de trouver la méthode '%s' pour l'URL '%s'.";
    public static final String METHOD_ERROR = "Le paramètre '%s' de la valeur '%s' n'a pas pu être converti en type '%s'";
    public static final String HTTP_CLIENT_ERROR = "Erreur client HTTP.";
    public static final String SERVER_INTERNAL_ERROR = "Erreur interne du serveur.";
    public static final String FORMAT_ERROR = "Erreur format pour lecture et écriture.";
    public static final String CONTRAINST_VALDATION_ERROR = "Erreur violation de cahmps ou attributs.";
    public static final String NOT_FOUND_ERROR = "Erreur recherche infructueuse de données.";
    public static final String INTEGRITY_ERROR = "Erreur de violations d'intégrité des données.";
    public static final String ACCESS_DENIED = "Accès non autorisés.";

    
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
    public static final String PAYS_MSG = "Le pays de résidence est obligatoire.";
    public static final String PAYS_SIZE_MSG = "Le pays doit contenir au plus 50 caractères.";

    public static final int CINQUANTE = 50;
    public static final int QUATRE_VINGT = 80;

    /**
     * Construire la réponse HTTP avec le status et le corps.
     * 
     * @param <T>               le type de l'objet embarquée.
     * @param pResponseErrorDTO entité de gestion des erreurs pour la réponse.
     * @return entité générique de la réponse avec le statut HTTP.
     */
    public static <T> ResponseEntity<GenericApiResponse<T>> buildResponseErrorEntity(final ResponseErrorDTO pResponseErrorDTO)
    {
        @SuppressWarnings("unchecked")
        final GenericApiResponse<T> response = (GenericApiResponse<T>) GenericApiResponse.builder()//
        .errors(pResponseErrorDTO)//
        .build();

        return ResponseEntity//
        .status(pResponseErrorDTO.getStatus())//
        .body(response);
    }
}
