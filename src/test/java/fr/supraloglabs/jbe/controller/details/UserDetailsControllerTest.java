/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserDetailsControllerTest.java
 * Date de création : 19 févr. 2021
 * Heure de création : 09:03:39
 * Package : fr.supraloglabs.jbe.controller.details
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.controller.details;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.supraloglabs.jbe.TestsDataUtils;
import fr.supraloglabs.jbe.config.AppRootConfig;
import fr.supraloglabs.jbe.controller.details.UserDetailsController;
import fr.supraloglabs.jbe.model.po.User;
import fr.supraloglabs.jbe.service.mapper.UserMapper;
import fr.supraloglabs.jbe.service.user.UserService;
import fr.supraloglabs.jbe.util.UserAccountUtil;

/**
 * Classe des Tests Unitaires des objets de type {@link UserDetailsController}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "userDetailsControllerTest", classes = { AppRootConfig.class, UserMapper.class, UserService.class,
        UserDetailsController.class })
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserDetailsControllerTest
{
    private static final String API_URL = "/api-users/user/search/{id}?country=France";
    private static final String API_URL_QUERY_PARAM = "/api-users/user/search/{id}?country=Italie";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;

    private UserDetailsController controller;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.controller = new UserDetailsController(userService, userMapper);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.controller = null;
        this.userMapper = null;
        this.userService = null;
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.controller.details.UserDetailsController#getUSerDetails(java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    void testGetUSerDetails() throws Exception
    {
        final User searchedUser = TestsDataUtils.USER;
        final Optional<User> userOptional = Optional.of(searchedUser);
        BDDMockito.given(userService.getUserById(Mockito.any(String.class))).willReturn(userOptional);

        final String strContent = this.objectMapper.writeValueAsString(searchedUser);

        this.mockMvc.perform(MockMvcRequestBuilders.get(API_URL, new String(searchedUser.getId()))//
        .characterEncoding(StandardCharsets.UTF_8.name())//
        .contentType(MediaType.APPLICATION_JSON)//
        .accept(MediaType.APPLICATION_JSON)//
        .headers(TestsDataUtils.httpHeaders())//
        .content(strContent)//
        )//
        .andDo(MockMvcResultHandlers.print())//
        .andExpect(status().isOk())//
        .andExpect(jsonPath("$.id").value(searchedUser.getId()))//
        .andExpect(jsonPath("$.lastName").value(searchedUser.getLastName()))//
        .andExpect(jsonPath("$.firstName").value(searchedUser.getFirstName()))//
        .andExpect(jsonPath("$.age").value(searchedUser.getAge()))//
        .andExpect(jsonPath("$.email").value(searchedUser.getEmail()))//
        ;

        assertThat(this.controller).isNotNull();
    }

    @Test
    void testGetUSerDetails_With_Bad_Id() throws Exception
    {
        final User searchedUser = TestsDataUtils.USER;
        final Optional<User> userOptional = Optional.of(searchedUser);
        BDDMockito.given(userService.getUserById(Mockito.any(String.class))).willReturn(userOptional);

        final String strContent = this.objectMapper.writeValueAsString(searchedUser);

        this.mockMvc.perform(MockMvcRequestBuilders.get(API_URL, new String(searchedUser.getId() + "4587"))//
        .characterEncoding(StandardCharsets.UTF_8.name())//
        .contentType(MediaType.APPLICATION_JSON)//
        .accept(MediaType.APPLICATION_JSON)//
        .headers(TestsDataUtils.httpHeaders())//
        .content(strContent)//
        )//
        .andDo(MockMvcResultHandlers.print())//
        .andExpect(status().isNotFound())//
        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.toString()))//
        .andExpect(jsonPath("$.details").exists())//
        .andExpect(jsonPath("$.timestamp").exists())//
        .andExpect(jsonPath("$.message").value(UserAccountUtil.FIND_BY_ID_MSG))//
        ;
    }

    @Test
    void testGetUSerDetails_With_Null_Id() throws Exception
    {
        final User searchedUser = TestsDataUtils.USER;
        final Optional<User> userOptional = Optional.of(searchedUser);
        BDDMockito.given(userService.getUserById(Mockito.any(String.class))).willReturn(userOptional);

        final String strContent = this.objectMapper.writeValueAsString(searchedUser);

        this.mockMvc.perform(MockMvcRequestBuilders.get(API_URL, new String("null"))//
        .characterEncoding(StandardCharsets.UTF_8.name())//
        .contentType(MediaType.APPLICATION_JSON)//
        .accept(MediaType.APPLICATION_JSON)//
        .headers(TestsDataUtils.httpHeaders())//
        .content(strContent)//
        )//
        .andDo(MockMvcResultHandlers.print())//
        .andExpect(status().isNotFound())//
        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.toString()))//
        .andExpect(jsonPath("$.details").exists())//
        .andExpect(jsonPath("$.timestamp").exists())//
        .andExpect(jsonPath("$.message").value(UserAccountUtil.FIND_BY_ID_MSG))//
        ;
    }

    @Test
    void testGetUSerDetails_With_Bad_Country() throws Exception
    {
        final User searchedUser = TestsDataUtils.USER;

        final Optional<User> userOptional = Optional.of(searchedUser);
        BDDMockito.given(userService.getUserById(Mockito.any(String.class))).willReturn(userOptional);

        final String strContent = this.objectMapper.writeValueAsString(searchedUser);
        final String message = UserAccountUtil.buildMessage(searchedUser.getId());

        this.mockMvc.perform(MockMvcRequestBuilders.get(API_URL_QUERY_PARAM, new String(searchedUser.getId()))//
        .characterEncoding(StandardCharsets.UTF_8.name())//
        .contentType(MediaType.APPLICATION_JSON)//
        .accept(MediaType.APPLICATION_JSON)//
        .headers(TestsDataUtils.httpHeaders())//
        .content(strContent)//
        )//
        .andDo(MockMvcResultHandlers.print())//
        .andExpect(status().isNotFound())//
        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.toString()))//
        .andExpect(jsonPath("$.details").exists())//
        .andExpect(jsonPath("$.timestamp").exists())//
        .andExpect(jsonPath("$.message").value(message))//
        ;
    }

}
