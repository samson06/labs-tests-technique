/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserUpdateDeleteGetAllController.java
 * Date de création : 20 févr. 2021
 * Heure de création : 05:20:07
 * Package : fr.supraloglabs.jbe.controller.update
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.controller.update;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

/**
 * Controlleur REST permettant de :
 * <ul>
 * <li>Afficher la liste paginée triée par le nom des utilisateurs enregistrés dan le SI.</li>
 * <li>Mettre à jour les données d'un utilisateur enregistré dans le SI.</li>
 * <li>Supprimer les données d'un utilisateur enregistré dans le SI.</li>
 * </ul>
 * 
 * @author Vincent Otchoun
 */
@Api(value = "User Update Delete GetAllPageable Rest Controller: opérations de mise à jour, suppression et affichage liste paginée des données des utilisateurs enrigistrés dans le SI")
@RestController
@RequestMapping("/api-users")
@Validated
public class UserUpdateDeleteGetAllController
{
    //
    private final UserMapper userMapper;
    private final UserService userService;

    /**
     * Constructeur avec en paramètres les beans pour injection en dépendances.
     * 
     * @param pUserMapper  le service de conversion des objets selon leur type.
     * @param pUserService le service des opérations d'accès aux données utilisateur en base.
     */
    @Autowired
    public UserUpdateDeleteGetAllController(final UserMapper pUserMapper, final UserService pUserService)
    {
        this.userMapper = pUserMapper;
        this.userService = pUserService;
    }

    /**
     * Obtenir la lsite paginée de l'ensemble des utilisateurs triée par le nom de l'utilisateur.
     * 
     * @param pPageable condition de pagination de la liste (index de la page, nombre d'éléments dans la page à retourner).
     * @return la liste paginéee des utilisateurs enregistrés dans le SI.
     */
    @GetMapping("/user/users")
    @ApiOperation(value = "Afficher la liste paginée des utilisateur enreigistrés dans le SI", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "<font color='#096a09'>Opération réussie</font> - <font color='#730800'>Renvoie la liste paginée des utilisateurs enregistrés</font>."),
            @ApiResponse(responseCode = "404", description = "<font color='#e63f1b'>Opération en échec</font>  - <font color='#26c4ec'>Renvoie le message d'erreur correspondant</font>.") })
    public ResponseEntity<List<UserDTO>> getUsersByPageable(@PageableDefault(size = Integer.MAX_VALUE, sort = { "lastName" }) Pageable pPageable)
    {
        final Page<User> page = this.userService.getAllUsersPageable(pPageable);
        if (page.isEmpty())
        {
            throw new AppCustomException(UserAccountUtil.ALL_USERS_EMPTY);
        }

        final Collection<User> users = page.getContent();
        final List<UserDTO> dtos = (List<UserDTO>) this.userMapper.toUserDtos(users);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Mettre à jour les infromations d'un utilisateur existant dans le système d'informations.
     * 
     * @param pUserId identifiant de l'utilisateur à mettre à jour.
     * @param dto     les données de l'utilisateur à mettre à jour.
     * @return OK si succès, 404 sinon.
     */
    @PutMapping("/user/update/{id}")
    @ApiOperation(value = "Mettre à jour les données d'un utilisateur enregistré dans le SI", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "<font color='#096a09'>Opération réussie</font> - <font color='#730800'>Renvoie les données de l'utilisateur mis à jour</font>."),
            @ApiResponse(responseCode = "404", description = "<font color='#e63f1b'>Opération en échec</font>  - <font color='#26c4ec'>Renvoie le message d'erreur correspondant</font>.") })
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "id", required = true) final String pUserId, @RequestBody final UserDTO dto)
    {
        final User user = this.userMapper.convertToUser(dto);
        final User responseUser = this.userService.updateUser(pUserId, user);
        final UserDTO responseDto = this.userMapper.convertToUserDTO(responseUser);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Supprimer les données d'un utilisateur du système d'informations.
     * 
     * @param userId identifiant de l'utilisateur à supprimer.
     * @return OK si succès, 404 sinon.
     */
    @DeleteMapping("/user/destroy/{id}")
    @ApiOperation(value = "Supprimer les données d'un utilisateur enregistré dans le SI", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "<font color='#096a09'>Opération réussie</font> - <font color='#730800'>Renvoie OK</font>."),
            @ApiResponse(responseCode = "404", description = "<font color='#e63f1b'>Opération en échec</font>  - <font color='#26c4ec'>Renvoie le message d'erreur correspondant</font>.") })
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id", required = true) final String userId)
    {
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(UserAccountUtil.DELETE_OK_MSG, HttpStatus.OK);
    }
}
