/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : ValidationErrorDTO.java
 * Date de création : 15 févr. 2021
 * Heure de création : 08:02:48
 * Package : fr.supraloglabs.jbe.model.dto
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.model.dto.error;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Objet d'encapsulation des erreurs de validation pour des paramètres ou attibuts des objets entrée.
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
@JsonPropertyOrder({ "object", "field", "rejectedValue", "message" })
public class ValidationErrorDTO
{
    private String object; // Objet à traiter en validation

    private String field; // Le champ à valider

    private Object rejectedValue; // La valeur de rejet de la validation

    private String message; // Le message d'erreur de validation

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
