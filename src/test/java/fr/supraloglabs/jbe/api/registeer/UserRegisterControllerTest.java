/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserRegisterControllerTest.java
 * Date de création : 18 févr. 2021
 * Heure de création : 13:53:15
 * Package : fr.supraloglabs.jbe.api.registeer
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.api.registeer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
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
import fr.supraloglabs.jbe.model.dto.UserDTO;
import fr.supraloglabs.jbe.model.po.User;
import fr.supraloglabs.jbe.service.mapper.UserMapper;
import fr.supraloglabs.jbe.service.user.UserService;
import fr.supraloglabs.jbe.util.UserAccountUtil;

/**
 * Classe des Tests Unitraires des objets de type {@link UserRegisterController}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "userRegisterControllerTest", classes = { AppRootConfig.class, UserMapper.class, UserService.class,
        UserRegisterController.class })
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserRegisterControllerTest
{
    // server.
    private static final String API_URL = "/api-users/user/register";
    private static final String DETAILS = "uri=/api-users/user/register";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private MongoOperations mongoOps;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    
    private UserRegisterController controller;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.controller = new UserRegisterController(this.userService, this.userMapper);
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
        this.mongoOps.dropCollection(User.class);
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.api.registeer.UserRegisterController#createNewUSerDetails(fr.supraloglabs.jbe.model.dto.UserDTO)}.
     * 
     * @throws Exception
     */
    @DisplayName("given object to save when save object using UserMapper and UserService then object is saved")
    @Test
    void testCreateNewUSerDetails() throws Exception
    {
        final UserDTO userMock = UserDTO.builder()//
        .lastName("Batcho")//
        .firstName("Karen Djayé")//
        .email("karen6.test@live.fr")//
        .age(28)//
        .country("France")//
        .adresse("23 Rue du Commandant Mages")//
        .city("Marseille")//
        .phone("0645789512")//
        .build();

        BDDMockito.given(this.userMapper.convertToUser(Mockito.any(UserDTO.class))).willReturn(TestsDataUtils.USER_TEST);
        BDDMockito.given(userService.createUser(Mockito.any(User.class))).willReturn(TestsDataUtils.USER_TEST);
        BDDMockito.given(this.userMapper.convertToUserDTO(Mockito.any(User.class))).willReturn(userMock);
        BDDMockito.given(this.userService.getByEmailIgnoreCase(Mockito.any(String.class))).willReturn(Collections.emptyList());
        //

        final String strContent = this.objectMapper.writeValueAsString(userMock);

        this.mockMvc.perform(MockMvcRequestBuilders.post(API_URL)//
        .characterEncoding(StandardCharsets.UTF_8.name())//
        .contentType(MediaType.APPLICATION_JSON)//
        .accept(MediaType.APPLICATION_JSON)//
        .headers(TestsDataUtils.httpHeaders())//
        .content(strContent)//
        )//
        .andDo(MockMvcResultHandlers.print())//
        .andExpect(status().isOk())//
        .andExpect(jsonPath("$.lastName").value(userMock.getLastName()))//
        .andExpect(jsonPath("$.firstName").value(userMock.getFirstName()))//
        .andExpect(jsonPath("$.age").value(userMock.getAge()))//
        .andExpect(jsonPath("$.email").value(userMock.getEmail()))//
        ;

        assertThat(this.controller).isNotNull();

        verify(this.userService, times(0)).createUser(Mockito.any(User.class));
    }

    @Test
    void testCreateNewUSerDetails_WithNotValidUSer() throws Exception
    {
        final UserDTO userMock = UserDTO.builder()//
        .lastName("Batcho")//
        .firstName("Karen Djayé")//
        .email("karen6.test@live.fr")//
        .age(15)//
        .country("France")//
        .adresse("23 Rue du Commandant Mages")//
        .city("Marseille")//
        .phone("0645789512")//
        .build();

        BDDMockito.given(this.userMapper.convertToUser(userMock)).willReturn(TestsDataUtils.USER_TEST);
        BDDMockito.given(this.userMapper.convertToUserDTO(TestsDataUtils.USER_TEST)).willReturn(userMock);

        final String strContent = this.objectMapper.writeValueAsString(userMock);
        this.mockMvc.perform(MockMvcRequestBuilders.post(API_URL)//
        .characterEncoding(StandardCharsets.UTF_8.name())//
        .contentType(MediaType.APPLICATION_JSON)//
        .accept(MediaType.APPLICATION_JSON)//
        .headers(TestsDataUtils.httpHeaders())//
        .content(strContent)//
        )//
        .andDo(MockMvcResultHandlers.print())//
        .andExpect(status().isInternalServerError())//
        .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.toString()))//
        .andExpect(jsonPath("$.details").value(DETAILS))//
        ;
    }

    @Test
    void testCreateNewUSerDetails_WithId() throws Exception
    {
        final UserDTO userMock = UserDTO.builder()//
        .id("1L")//
        .lastName("Batcho")//
        .firstName("Karen Djayé")//
        .email("test.test@live.fr")//
        .age(20)//
        .country("France")//
        .adresse("23 Rue du Commandant Mages")//
        .city("Marseille")//
        .phone("0645789512")//
        .build();

        final String strContent = this.objectMapper.writeValueAsString(userMock);
        this.mockMvc.perform(MockMvcRequestBuilders.post(API_URL)//
        .characterEncoding(StandardCharsets.UTF_8.name())//
        .contentType(MediaType.APPLICATION_JSON)//
        .accept(MediaType.APPLICATION_JSON)//
        .headers(TestsDataUtils.httpHeaders())//
        .content(strContent)//
        )//
        .andDo(MockMvcResultHandlers.print())//
        .andExpect(status().isNotFound())//
        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.toString()))//
        .andExpect(jsonPath("$.details").value(DETAILS))//
        .andExpect(jsonPath("$.message").value(UserAccountUtil.ALREADY_EXIST_USER_ID_MSG))//
        ;
    }

    @Test
    void testCreateNewUSerDetails_WithEmptyMail() throws Exception
    {
        final UserDTO userMock = UserDTO.builder()//
        .lastName("Batcho")//
        .firstName("Karen Djayé")//
        .email(StringUtils.EMPTY)//
        .age(15)//
        .country("France")//
        .adresse("23 Rue du Commandant Mages")//
        .city("Marseille")//
        .phone("0645789512")//
        .build();

        final String strContent = this.objectMapper.writeValueAsString(userMock);
        this.mockMvc.perform(MockMvcRequestBuilders.post(API_URL)//
        .characterEncoding(StandardCharsets.UTF_8.name())//
        .contentType(MediaType.APPLICATION_JSON)//
        .accept(MediaType.APPLICATION_JSON)//
        .headers(TestsDataUtils.httpHeaders())//
        .content(strContent)//
        )//
        .andDo(MockMvcResultHandlers.print())//
        .andExpect(status().isInternalServerError())//
        .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.toString()))//
        .andExpect(jsonPath("$.details").value(DETAILS))//
        ;
    }
}
