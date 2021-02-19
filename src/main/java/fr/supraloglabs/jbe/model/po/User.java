/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : User.java
 * Date de création : 15 févr. 2021
 * Heure de création : 01:14:35
 * Package : fr.supraloglabs.jbe.model.po
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.model.po;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
 * Mapping objet des données des utilisateurs dans le système d'informations.
 * 
 * @author Vincent Otchoun
 */
@Document(collection = "C_USERS")  // Le nom de la collection dans la base de données
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User implements Serializable
{
    /**
     * 
     */
    @Transient
    private static final long serialVersionUID = 4677921064921552486L;

    @Id
    String id; // identifiant technique auto-généré

    @NotNull(message = UserAccountUtil.PRENOM_MSG)
    @Size(min = 1, max = UserAccountUtil.QUATRE_VINGT, message = UserAccountUtil.PRENOM_SIZE_MSG)
    @Field("firstName")
    String firstName; // le prénom de l'utilisateur

    @NotNull(message = UserAccountUtil.NOM_MSG)
    @Size(min = 1, max = UserAccountUtil.CINQUANTE, message = UserAccountUtil.NOM_SIZE_MSG)
    @Field("lastName")
    String lastName; // le nom de lisateur

    @NotNull(message = UserAccountUtil.EMAIL_MSG)
    @Size(min = 1, max = UserAccountUtil.CINQUANTE, message = UserAccountUtil.EMAIL_SIZE_MSG)
    @Email
    @Field("email")
    @Indexed(unique = true)
    String email; // adresse mail de l'utilisateur

    @NotNull(message = UserAccountUtil.AGE_MSG)
    @Field("age")
    @Min(value = 18, message = UserAccountUtil.AGE_MIN_MSG)
    Integer age; // l'âge de l'utilisateur

    @NotNull(message = UserAccountUtil.PAYS_MSG)
    @Size(min = 1, max = UserAccountUtil.CINQUANTE, message = UserAccountUtil.PAYS_SIZE_MSG)
    @Field("country")
    String country; // le pays de l'utilisateur

    @Field("adresse")
    String adresse; // adresse lieu de résidence de l'utilisateur

    @Field("city")
    String city; // la ville de résidence de l'utilisateur

    @Field("phone")
    String phone; // le numéro de téléphone de l'utilisateur

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
