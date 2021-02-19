/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserMapper.java
 * Date de création : 16 févr. 2021
 * Heure de création : 10:46:44
 * Package : fr.supraloglabs.jbe.service.mapper
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.service.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.supraloglabs.jbe.model.dto.UserDTO;
import fr.supraloglabs.jbe.model.po.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Service de conversion/transformation d'un objet de type {@link User} en son objet de tranfert de données
 * {@link UserDTO} et vice versa.
 * 
 * @author Vincent Otchoun
 */
@Service
@Slf4j
public class UserMapper
{

    private final ModelMapper modelMapper;

    /**
     * Constructeur avec paramètre pour injection du beans en dépendances.
     * 
     * @param modelMapper le bean de conversion des modèles selon le type.
     */
    @Autowired
    public UserMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    /**
     * Convertir l'objet {@link UserDTO} en {@link User}.
     * 
     * @param pUserDTO les données de l'objet de l'objet de transfert des informations de l'utilisateur.
     * @return les données en base de l'utilisateur.
     */
    public User convertToUser(final UserDTO pUserDTO)
    {
        log.info("[convertToUser] - Obtenir les données en base de l'utilisateur.");

        if (pUserDTO == null)
        {
            return null;
        }

        return this.modelMapper.map(UserDTO.class, User.UserBuilder.class)//
        .firstName(pUserDTO.getFirstName())//
        .lastName(pUserDTO.getLastName())//
        .email(pUserDTO.getEmail())//
        .age(pUserDTO.getAge())//
        .country(pUserDTO.getCountry())//
        .adresse(pUserDTO.getAdresse())//
        .city(pUserDTO.getCity())//
        .phone(pUserDTO.getPhone())//
        .build();
    }

    /**
     * Convertir l'objet {@link User} en {@link UserDTO}.
     * 
     * @param pUser les données en base de l'utilisateur.
     * @return l'objet de transfert des informations de l'utilisateur.
     */
    public UserDTO convertToUserDTO(final User pUser)
    {
        log.info("[convertToUserDTO] - Obtenir les données de l'objet de transfert des données de l'utilisateur.");

        if (pUser == null)
        {
            return null;
        }

        return this.modelMapper.map(User.class, UserDTO.UserDTOBuilder.class)//
        .id(pUser.getId())//
        .firstName(pUser.getFirstName())//
        .lastName(pUser.getLastName())//
        .email(pUser.getEmail())//
        .age(pUser.getAge())//
        .country(pUser.getCountry())//
        .adresse(pUser.getAdresse())//
        .city(pUser.getCity())//
        .phone(pUser.getPhone())//
        .build();
    }

    /**
     * Construire la liste de {@link User} à partir de la liste de {@link UserDTO}.
     * 
     * @param userDTOs la liste des données des objets de transfert des informations de l'utilisateur.
     * @return la liste des données en base de données de l'utilisateur.
     */
    public Collection<User> toUsers(final Collection<UserDTO> userDTOs)
    {
        log.info("[toUsers] - Obtenir la liste des données en base des utilisateurs.");

        //
        return Optional.ofNullable(userDTOs)//
        .orElseGet(Collections::emptyList)//
        .stream()//
        .filter(Objects::nonNull)//
        .map(this::convertToUser)//
        .collect(Collectors.toList());
    }

    /**
     * Construire la liste des objets de transfert des données de {@link UserDTO} à partir de la liste des données de
     * l'utilisateur en base de données {@link User}.
     * 
     * @param users la liste des données en base de données de l'utilisateur.
     * @return la liste des données des objets de transfert des informations de l'utilisateur.
     */
    public Collection<UserDTO> toUserDtos(final Collection<User> users)
    {
        log.info("[toUserDtos] - Obtenir la liste des données des objets de transfert des données de l'utilisateur.");

        //
        return Optional.ofNullable(users)//
        .orElseGet(Collections::emptyList)//
        .stream()//
        .filter(Objects::nonNull)//
        .map(this::convertToUserDTO)//
        .collect(Collectors.toList());
    }
}
