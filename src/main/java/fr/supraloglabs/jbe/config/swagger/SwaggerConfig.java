/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : SwaggerConfig.java
 * Date de création : 21 févr. 2021
 * Heure de création : 15:28:09
 * Package : fr.supraloglabs.jbe.config.swagger
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.config.swagger;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.supraloglabs.jbe.util.UserAccountUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Configuration pour embarquer Swagger pour la documentation et Tests dans l'application.
 * 
 * @author Vincent Otchoun
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig
{

    /**
     * Docket du package de base fournissant l'ensemble des API du système d'information.
     * 
     * @return le bean à injecter.
     */
    @Bean
    public Docket defaultPackageControllerDocket()
    {
        return genericDocketBuilder(UserAccountUtil.DEFAULT_GROUPENAME, UserAccountUtil.DEFAULT_CONTROLLER_BASE_PACKAGE);
    }

    /**
     * Docket de l'API permettant de remonter les données d'un utilisateur existant dans le système d'informations.
     * 
     * @return le bean à injecter.
     */
    @Bean
    public Docket userDetailsControllerDocket()
    {
        return genericDocketBuilder(UserAccountUtil.USER_DETAILS_GROUPENAME, UserAccountUtil.USER_DETAILS_CONTROLLER_BASE_PACKAGE);
    }

    /**
     * Docket de l'API permettant d'enregistrer les données d'un nouvel utilisateur dans le système d'informations.
     * 
     * @return le bean à injecter.
     */
    @Bean
    public Docket userRegisterControllerDocket()
    {
        return genericDocketBuilder(UserAccountUtil.REGISTER_USER_GROUPENAME, UserAccountUtil.REGISTER_USER_CONTROLLER_BASE_PACKAGE);
    }

    /**
     * Docket de l'API fournissant les opérations de mise à jour, suppression, affichage de la lsite paginée.
     * 
     * @return le bean à injecter.
     */
    @Bean
    public Docket userOtherOpsControllerDocket()
    {
        return genericDocketBuilder(UserAccountUtil.AUTRES_USER_OPERATIONS_GROUPENAME,
        UserAccountUtil.AUTRES_USER_OPERATIONS_CONTROLLER_BASE_PACKAGE);
    }

    /**
     * Construire la Docket à partir du nom du groupe et le nom du package de base.
     * 
     * @param pGroupeName  le nom du groupe de rattachement de l'API
     * @param pBasePackage le nom du package de base de l'API.
     * @return le Docket contruit.
     */
    private Docket genericDocketBuilder(final String pGroupeName, final String pBasePackage)
    {
        return new Docket(DocumentationType.OAS_30)//
        .groupName(pGroupeName)//
        .select()//
        .apis(RequestHandlerSelectors.basePackage(pBasePackage))//
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))// allows selection of RequestHandler's
        .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))//
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))//
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiResponses.class))//
        .paths(PathSelectors.any())//
        .build()//
        .apiInfo(this.metadata())//
        .produces(UserAccountUtil.MEDIA_SET)//
        .consumes(UserAccountUtil.MEDIA_SET)//
        .securitySchemes(Collections.singletonList(this.apiKey()))//
        .securityContexts(Collections.singletonList(securityContext()))//
        .directModelSubstitute(java.time.LocalDate.class, String.class)// Convenience rule builder that substitutes
        .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)// Convenience rule builder that substitutes
        .directModelSubstitute(java.time.LocalDateTime.class, Date.class)// Convenience rule builder that substitutes
        .genericModelSubstitutes(ResponseEntity.class)// essayer avec Optional
        .genericModelSubstitutes(Optional.class)//
        ;
    }

    private ApiKey apiKey()
    {
        return new ApiKey(UserAccountUtil.AUTH_BAERER_TOKEN, UserAccountUtil.AUTHORISATION, UserAccountUtil.HEADER);
    }

    private ApiInfo metadata()
    {
        return new ApiInfoBuilder()//
        .title(UserAccountUtil.TITLE.trim())//
        .description(UserAccountUtil.DESCRITPION.trim())//
        .contact(this.contact())//
        .license(UserAccountUtil.LICENCE.trim())//
        .licenseUrl(UserAccountUtil.LICENCE_URL.trim())//
        .version(UserAccountUtil.VERSION.trim())//
        .build();
    }

    private Contact contact()
    {
        return new Contact(UserAccountUtil.CONTACT_NAME.trim(), UserAccountUtil.CONTACT_URL.trim(), UserAccountUtil.CONTACT_MAIL.trim());
    }

    @SuppressWarnings("deprecation")
    private SecurityContext securityContext()
    {
        return SecurityContext.builder().//
        securityReferences(defaultAuth())//
        .forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(UserAccountUtil.AUTHORISATION, authorizationScopes));
    }
}
