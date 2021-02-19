/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : ErrorDetails.java
 * Date de création : 15 févr. 2021
 * Heure de création : 08:13:11
 * Package : fr.supraloglabs.jbe.model.dto.error
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.model.error;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * Objet de mapping de la composante de gestion des erreurs dans les retours des API. Il sera intégré à l'objet de
 * gestion des retours de l'API.
 * 
 * @author Vincent Otchoun
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonPropertyOrder({ "status", "timestamp", "details", "validationErrors" })
public class ErrorDetails
{
    private HttpStatus status; // Le code retour HTTP et descriptif associé

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = UserAccountUtil.ISO_DATE_TIME, locale = UserAccountUtil.FR_LOCALE, timezone = UserAccountUtil.CET_TIMEZONE)
    private LocalDateTime timestamp; // Horodatage de constatation de l'erreur

    private String details; // Lee détails de l'erreur à afficher

    private String message; // Le message d'erreurs

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
