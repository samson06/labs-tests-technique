/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserService.java
 * Date de création : 17 févr. 2021
 * Heure de création : 12:51:42
 * Package : fr.supraloglabs.jbe.service.user
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.service.user;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import fr.supraloglabs.jbe.error.AppCustomException;
import fr.supraloglabs.jbe.model.po.User;
import fr.supraloglabs.jbe.repository.UserRepository;
import fr.supraloglabs.jbe.util.UserAccountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Service fournissant les fonctionnalités de gestion des données utilisateur dans le SI.
 * 
 * @author Vincent Otchoun
 */
@Service(value = "userService")
@Transactional
@Slf4j
public class UserService
{
    private final UserRepository userRepository;

    /**
     * Constructeur avec paramètre pour injection du bean dépendancess.
     * 
     * @param pUserRepository le DAO des opérations d'accès aux données utilisateur en base.
     */
    @Autowired
    public UserService(final UserRepository pUserRepository)
    {
        this.userRepository = pUserRepository;
    }

    /**
     * Enrregistrer les données d'un nouvel utilisateur dans le SI.
     * 
     * @param pUser les données de l'utilisateur à enregistrer.
     * @return les détails de l'utilisateur créé.
     */
    public User createUser(final User pUser)
    {
        log.info("[createUser] - Engeristrer les détails d'un nouvel utilisateur dans le SI.");

        // Tentative d'enregistrement des données d'un nouvel utilisateur.
        try
        {
            final User user = this.userRepository.save(pUser);
            Assert.notNull(user, UserAccountUtil.SAVE_MSG);
            return user;
        }
        catch (Exception e)
        {
            throw new AppCustomException(e);
        }
    }

    /**
     * Recherche les données des utlisateurs en base de données par leur adresse mail.
     * 
     * @param pEmail adresse email des utilisateurs recherchés.
     * @return la liste des données des utilisateurs correspaondant au critère de recherche.
     */
    @Transactional(readOnly = true)
    public Collection<User> getByEmailIgnoreCase(String pEmail)
    {
        log.info("[getByEmailIgnoreCase] - Obtenir les détails utilisateur par son email. Email=[{}].", pEmail);

        return this.userRepository.findByEmailIgnoreCase(pEmail)//
        .stream()//
        .filter(Objects::nonNull)//
        .collect(Collectors.toList());
    }

    /**
     * Obtenir les détails de l'utilisateur par son identifiant.
     * 
     * @param pId identifiant de l'utilisateur dans la base de données.
     * @return les détails de l'utilisateur recherché si existe, sinon vide.
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(final String pId)
    {
        log.info("[getUserById] - Obtenir les détails d'un utilisateur par son identifiant. Id=[{}].", pId);

        return Optional.ofNullable(this.userRepository.findOneById(pId)).filter(Optional::isPresent)//
        .orElseThrow(() -> new AppCustomException(UserAccountUtil.FIND_BY_ID_MSG));
    }

    /**
     * Rechercher la liste paginée des détails en base de données de l'ensemble des utilisateurs du SI.
     * 
     * @param pPageable condition de pagination de la liste (index de la page, nombre d'éléments dans la page à retourner).
     * @return la liste paginée des informations recherchées.
     */
    @Transactional(readOnly = true)
    public Page<User> getAllUsersPageable(final Pageable pPageable)
    {
        log.info("[getAllUsersPageable] - Obtenir la liste paginée des détails de l'ensemble des utilisateurs du SI.");
        return this.userRepository.findAll(pPageable);
    }

    /**
     * Obtenir la liste des détails de l'ensemble des utilisateurs du SI.
     * 
     * @return la liste des détails des utilisateurs enregistrés dans le SI.
     */
    @Transactional(readOnly = true)
    public Collection<User> getUsers()
    {
        log.info("[getUsers] - Obtenir la liste des détails de l'ensemble des utilisateurs du SI.");

        return this.userRepository.findAll();
    }

    /**
     * Supprimer les détails d'un utilisateur du SI à partir de son identifiant.
     * 
     * @param pUserId identifiant de l'utilisateur dans la base de données.
     */
    public void deleteUser(final String pUserId)
    {
        log.info("[deleteUser] - Supprimer les informations d'un utilisateur du SI. Identifiant [{}]", pUserId);

        // Tentative de suppression des informations d'un utilisateur du SI.
        try
        {
            this.getUserById(pUserId)//
            .ifPresent(this.userRepository::delete);
        }
        catch (Exception e)
        {
            throw new AppCustomException(e);
        }
    }

    /**
     * Mettre à jour les détails d'un utilisateur dans la base de données à partir de son identifiant.
     * 
     * @param pUserId identifiant de l'utilisateur recherché pour mise à jour des détails.
     * @param pUser   détails de l'utilisateur à mettre à jour.
     */
    public void updateUser(final String pUserId, final User pUser)
    {
        log.info("[updateUser] - Mise à jour des informations d'untilisateur existant. Identifiant : [{}].", pUserId);

        // Tentative des mise à jour des informations d'un utilisateur existant dans le SI.
        try
        {
            this.getUserById(pUserId)//
            .ifPresent(user -> {
                final String id = user.getId();
                pUser.setId(id);
                this.createUser(pUser);
            });
        }
        catch (Exception e)
        {
            throw new AppCustomException(e);
        }
    }
}
