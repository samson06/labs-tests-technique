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

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import fr.supraloglabs.jbe.model.po.User;

/**
 * Référentiel Spring Data MongoDB pour les opérations en base de données sur la collection persistant les données de
 * l'entité {@link User}.
 * 
 * @author Vincent Otchoun
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>
{
    /**
     * Recherche les données des utlisateurs en base de données par leur adresse mail.
     * 
     * @param pEmail adresse email de l'utilisateur recherché.
     * @return la liste des données des utilisateurs correspondants.
     */
    List<User> findByEmailIgnoreCase(final String pEmail);

    /**
     * Obtenir les détails en base de données de l'utilisateur à partir de son identifiant.
     * 
     * @param pId identifiant technique de l'objet en base de données.
     * @return les détails de l'utilisateur recherché s'il existe, sinon vide.
     */
    Optional<User> findOneById(final String pId);
}
