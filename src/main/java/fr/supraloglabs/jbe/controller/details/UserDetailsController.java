/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserDetailsController.java
 * Date de création : 19 févr. 2021
 * Heure de création : 06:45:05
 * Package : fr.supraloglabs.jbe.controller.details
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.controller.details;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
 * Contrôleur REST pour afficher les détails d'un utilisateur existant dans le SI.
 * 
 * @author Vincent Otchoun
 */
@Api(value = "User Details Rest Controller: opération pour afficher les détails de l'utilisateur existant dans le SI")
@RestController
@RequestMapping("/api-users")
@Slf4j
@Validated
public class UserDetailsController
{
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Constructeur avec en paramètres les beans pour injection en dépendances.
     * 
     * @param pUserService le service des opérations d'accès aux données utilisateur en base.
     * @param pUserMapper  le service de conversion des objets selon leur type.
     */
    public UserDetailsController(UserService pUserService, UserMapper pUserMapper)
    {
        this.userService = pUserService;
        this.userMapper = pUserMapper;
    }

    /**
     * Rechercher et Afficher les détails 'un utilisateur existant dans le système d'informations.
     * 
     * @param userId
     * @param userCountry le pays de résidence de l'utilisateur.
     * @return les données de l'utilisateur recherché en cas de succès, sinon les détails d'erreurs surnvenues.
     */
    @GetMapping("/user/search/{id}")
    @ApiOperation(value = "Afficher les détails d'un utilisateur existant dans le SI", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "<font color='#096a09'>Opération réussie</font>  - <font color='#730800'>Renvoie les données de l'utilisateur existant recherché</font>."),
            @ApiResponse(responseCode = "404", description = "<font color='#e63f1b'>Opération en échec - <font color='#26c4ec'>Renvoie le message d'erreur correspondant</font>.") })
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable(value = "id", required = true) final String userId,
    @RequestParam(value = "country", required = false, defaultValue = "France") final String userCountry)
    {
        log.info("[getUSerDetails] - Afficher les détails de l'utilisateur. Identifiant recherché : [{}].", userId);

        // Chech valeur en entrée identifiant fourni
        Assert.notNull(userId, UserAccountUtil.USER_ID_NOT_NULL_MSG + userId);

        // Rechercher l'utilisateur dans la base de données
        final Optional<User> userFromDb = this.userService.getUserById(userId);
        final boolean existUserDetails = userFromDb.isPresent();

        // Si l'utilisateur n'existe pas, construire et remmonter le message d'erreurs associé
        if (!existUserDetails)
        {
            UserAccountUtil.buildExcemption(userId);
        }

        // L'utilisateur existe.
        final User user = existUserDetails ? userFromDb.get() : null;

        // L'utilisateur existe, verifier qu'il réside bien en France, sinon remonter le message d'erreurs associé.
        final String pays = user != null ? user.getCountry() : null;
        final boolean isFranceCountry = StringUtils.isNotBlank(pays) && UserAccountUtil.IGNORE_CASE.apply(pays, userCountry);
        if (!isFranceCountry)
        {
            UserAccountUtil.buildExcemption(userId);
        }

        // Convertir en objet de transfert de données et retourner le résultat
        final UserDTO result = this.userMapper.convertToUserDTO(user);
        return ResponseEntity.ok(result);
    }
}
