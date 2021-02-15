/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : ResponseErrorDTO.java
 * Date de création : 15 févr. 2021
 * Heure de création : 08:13:11
 * Package : fr.supraloglabs.jbe.model.dto.error
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.model.dto.error;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import fr.supraloglabs.jbe.util.UserAccountUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Objet de mapping de la composante de gestion des erreurs dans les retours des API. Il sera intégréà l'objet de
 * gestion des retours.
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
@JsonPropertyOrder({ "status", "timestamp", "details", "validationErrors" })
public class ResponseErrorDTO
{
    private HttpStatus status; // Le code retour HTTP et descriptif associé

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = UserAccountUtil.ISO_DATE_TIME, locale = UserAccountUtil.FR_LOCALE, timezone = UserAccountUtil.CET_TIMEZONE)
    private LocalDateTime timestamp; // Horodatage de constatation de l'erreur

    private String details; // Le message d'erreurs à afficher

    private String debugMessage;

    private List<ValidationErrorDTO> validationErrors; // Erreurs de validation à afficher

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * Ajouter les objets d'encapsulation des erreurs de champs ou d'attributs.
     * 
     * @param fieldErrors la listes des champs en erreurs.
     */
    public void addFieldErrorValidationErrors(final List<FieldError> fieldErrors)
    {
        fieldErrors.forEach(this::addValidationErrorWithFieldError);
    }

    /**
     * Ajouter les objets d'encapsulation des erreurs de violsation de contraintes à la liste des erreurs de validations.
     * 
     * @param constraintViolations lieste de données d'erreurs de violation de contraintes.
     */
    public void addValidationErrorsCV(Set<ConstraintViolation<?>> constraintViolations)
    {
        constraintViolations.forEach(this::addValidationErrorWithCV);
    }

    /**
     * Ajouter l'objet d'encapsulation des erreurs de champs ou d'attribut à la liste des erreurs de validations.
     * 
     * @param fieldError les données d'erreurs de champs ou attributs.
     */
    private void addValidationErrorWithFieldError(FieldError fieldError)
    {
        this.addValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
    }

    /**
     * Ajouter l'objet d'encapsulation des erreurs de violsation de contraintes à la liste des erreurs de validations.
     * 
     * @param cv les données d'erreurs de violation de contraintes.
     */
    private void addValidationErrorWithCV(ConstraintViolation<?> cv)
    {
        this.addValidationError(cv.getRootBeanClass().getSimpleName(), ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(), cv
        .getInvalidValue(), cv.getMessage());
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message)
    {
        this.addSubError(this.buildApiValidationError(object, field, rejectedValue, message));
    }

    /**
     * Populer la lsite des erreurs de validations des achamps ou attributs de l'objet.
     * 
     * @param validationError les informations des erreurs de validation des champs ou attributs.
     */
    private void addSubError(ValidationErrorDTO validationError)
    {
        if (this.validationErrors == null)
        {
            this.validationErrors = new ArrayList<>();
        }
        this.validationErrors.add(validationError);
    }

    /**
     * Construire l'instance de {@link ValidationErrorDTO } avec les paramètres fournis.
     * 
     * @param object        l'objet à valider.
     * @param field         le champ ou l'attribut à valider.
     * @param rejectedValue la valeur de rejet de la validation.
     * @param message       le message d'erreurs de la validation.
     * @return l'instance de l'objet de transfert des données d'erreurs de validation.
     */
    private ValidationErrorDTO buildApiValidationError(String object, String field, Object rejectedValue, String message)
    {
        return ValidationErrorDTO.builder()//
        .object(object)//
        .field(field)//
        .message(message)//
        .rejectedValue(rejectedValue)//
        .build();
    }
}
