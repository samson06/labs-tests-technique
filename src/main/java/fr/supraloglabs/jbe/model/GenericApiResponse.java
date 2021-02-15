/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : GenericApiResponse.java
 * Date de création : 15 févr. 2021
 * Heure de création : 09:29:08
 * Package : fr.supraloglabs.jbe.model
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import fr.supraloglabs.jbe.model.dto.error.ResponseErrorDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Classe générique pour la construction des réponse ou retours des API.
 * 
 * @author Vincent Otchoun
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Hides the constructor to force usage of the Builder
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericApiResponse<T>
{
    //
    private T data; // Les données de la réponse de l'API
    private ResponseErrorDTO errors; // Les erreus survenues lors de l'exécution
}
