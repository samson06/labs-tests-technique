/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserDTO.java
 * Date de création : 15 févr. 2021
 * Heure de création : 07:24:14
 * Package : fr.supraloglabs.jbe.model.po.dto
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
 * @author Vincent Otchoun
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE) 
@Builder
@JsonPropertyOrder({ "id", "firstName", "lastName", "email", "age", "accountLocked", "adresse", "city", "phone" })
public class UserDTO
{

    Long id; // identifiant technique auto-généré

    @NotEmpty(message = UserAccountUtil.PRENOM_MSG)
    @Size(min = 1, max = UserAccountUtil.QUATRE_VINGT, message = UserAccountUtil.PRENOM_SIZE_MSG)
    String firstName; // le prénom de l'utilisateur

    @NotEmpty(message = UserAccountUtil.NOM_MSG)
    @Size(min = 1, max = UserAccountUtil.CINQUANTE, message = UserAccountUtil.NOM_SIZE_MSG)
    String lastName; // le nom de lisateur

    @NotEmpty(message = UserAccountUtil.EMAIL_MSG)
    @Size(min = 1, max = UserAccountUtil.CINQUANTE, message = UserAccountUtil.EMAIL_SIZE_MSG)
    @Email
    String email; // adresse mail de l'utilisateur

    @NotEmpty(message = UserAccountUtil.AGE_MSG)
    Integer age; // l'âge de l'utilisateur

    @NotEmpty(message = UserAccountUtil.PAYS_MSG)
    @Size(min = 1, max = UserAccountUtil.CINQUANTE, message = UserAccountUtil.PAYS_SIZE_MSG)
    String country; // le pays de l'utilisateur

    String adresse; // adresse lieu de résidence de l'utilisateur

    String city; // la ville de résidence de l'utilisateur

    String phone; // le numéro de téléphone de l'utilisateur

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
