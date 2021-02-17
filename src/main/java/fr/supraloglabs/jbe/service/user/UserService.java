/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserService.java
 * Date de création : 16 févr. 2021
 * Heure de création : 15:36:45
 * Package : fr.supraloglabs.jbe.service.user
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.service.user;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.supraloglabs.jbe.model.po.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Service fournissant les fonctionnalités de gestion des utilisateurs du SI.
 * 
 * @author Vincent Otchoun
 */
@Service(value = "userService")
@Transactional
@Slf4j
public class UserService
{

    public User createUser(final User pUser)
    {
        return pUser;

    }

    public Optional<User> getByEmailIgnoreCase(String pEmail)
    {
        return null;

    }

    public Optional<User> getWithFirstNameAndLastNameById(Long pId)
    {
        return null;
    }

    public Page<User> getAllPagedUsers(Pageable pPageable)
    {
        return null;
    }

    public Collection<User> getAllUsers()
    {
        return null;
    }
}
