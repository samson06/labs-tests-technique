/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserRegisterController.java
 * Date de création : 18 févr. 2021
 * Heure de création : 07:40:05
 * Package : fr.supraloglabs.jbe.controller.register
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.controller.register;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.supraloglabs.jbe.error.AppCustomException;
import fr.supraloglabs.jbe.model.dto.UserDTO;
import fr.supraloglabs.jbe.model.po.User;
import fr.supraloglabs.jbe.service.mapper.UserMapper;
import fr.supraloglabs.jbe.service.user.UserService;
import fr.supraloglabs.jbe.util.UserAccountUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Contrôleur REST pour la gestion de l'enregistrement des données de nouveaux utilisateurs dans le SI.
 * 
 * @author Vincent Otchoun
 */
@Api(value = "User Read Rest Controller: opération pour enregistrer les données des utilisateurs dans le SI")
@RestController
@RequestMapping("/api-users")
@Slf4j
@Validated
public class UserRegisterController
{
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Constructeur avec en paramètres les beans pour injection en dépendances.
     * 
     * @param pUserService le service des opérations d'accès aux données utilisateur en base.
     * @param pUserMapper  le service de conversion des objets selon leur type.
     */
    @Autowired
    public UserRegisterController(final UserService pUserService, final UserMapper pUserMapper)
    {
        this.userService = pUserService;
        this.userMapper = pUserMapper;
    }

    /**
     * Enrégistrer les données d'un nouvel utilisateur dans le système d'informations.
     * 
     * @param pUserDTO les données du nouvel utilisateur.
     * @return les informations sauvegardées en cas de succès, sinon les détails d'erreurs surnvenues.
     */
    @PostMapping("/user/register")
    @ApiOperation(value = "Ajouter un nouvel utilisateur dans le SI", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "<font color='#096a09'>Opération réussie</font> - <font color='#730800'>Renvoie les données de l'utilisateur enregistré</font>."),
            @ApiResponse(responseCode = "404", description = "<font color='#e63f1b'>Opération en échec</font>  - <font color='#26c4ec'>Renvoie le message d'erreur correspondant</font>.") })
    public ResponseEntity<UserDTO> createNewUSerDetails(@RequestBody final UserDTO pUserDTO)
    {
        log.info("[createNewUSerDetails] - Demande REST pour enregistrer l'utilisateur : {}", pUserDTO);
        
        Assert.notNull(pUserDTO, UserAccountUtil.USER_DATA_NOT_NULL_MSG); 

        final String id = pUserDTO.getId();

        // Converision DTO en objet persistant
        final User userToBeSaved = this.userMapper.convertToUser(pUserDTO);

        // Vérifier que l'utilisateur a au moins 18 ans et habite bien la France.
        final boolean isOKDataToSaved = UserAccountUtil.isValidUser(userToBeSaved);

        // Vérfier que l'utilisateur n'existe pas déjà en base de données à travers l'email qui doit être unique.
        final String email = pUserDTO.getEmail();
        final List<User> uSers = (List<User>) this.userService.getByEmailIgnoreCase(email);

        // Check identifiant existant
        if (StringUtils.isNotBlank(id))
        {
            throw new AppCustomException(UserAccountUtil.ALREADY_EXIST_USER_ID_MSG);
        }
        else if (!isOKDataToSaved)
        {
            throw new AppCustomException(UserAccountUtil.MAJEUR_FR_USER_MSG);
        }
        else if (!CollectionUtils.isEmpty(uSers))
        {
            throw new AppCustomException(UserAccountUtil.ALREADY_EXIST_USER_EMAIL_MSG);
        }
        else
        {
            final User newUser = this.userService.createUser(userToBeSaved);
            final UserDTO userDTO = this.userMapper.convertToUserDTO(newUser);
            return ResponseEntity.ok(userDTO);
        }
    }
}
