/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserRepository.java
 * Date de création : 16 févr. 2021
 * Heure de création : 14:59:20
 * Package : fr.supraloglabs.jbe.repository
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import fr.supraloglabs.jbe.model.po.User;

/**
 * Référentiel Spring Data MongoDB pour les opérations en base ende données sur la collection persistant les données de
 * l'entité {@link User}.
 * 
 * @author Vincent Otchoun
 */
@Repository
public interface UserRepository extends MongoRepository<User, Long>
{
    /**
     * Obtenir les informations de l'utilisateur dans la base de données à partir de son adresse email sans tenir de la
     * casse.
     * 
     * @param pEmail adresse email de l'utilisateur recherché.
     * @return les informations de l'utilisateur recherché s'i existe, sinon vide.
     */
    Optional<User> findOneByEmailIgnoreCase(final String pEmail);

    /**
     * Obtenir les données de la base de données de l'utilisateur par nom et prénom à partir de son identifiant technque.
     * 
     * @param pId identifiant technique en base de données de l'enregistrement lié à l'utilisateur recherché.
     * @return les informations de l'utilisateur recherché s'i existe, sinon vide.
     */
    Optional<User> findOneWithFirstNameAndLastNameById(final Long pId);

    /**
     * Rechercher la liste paginée des informations dans la base de données des utilisateurs du SI.
     * 
     * @param pPageable condition de pagination de la liste (index de la page, nombre d'éléments dans la page à retourner).
     * @return
     */
    // Page<User> findAllPagedUsers(final Pageable pPageable);

}
