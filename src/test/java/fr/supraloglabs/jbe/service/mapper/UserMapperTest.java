/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserMapperTest.java
 * Date de création : 16 févr. 2021
 * Heure de création : 11:29:58
 * Package : fr.supraloglabs.jbe.service.mapper
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import fr.supraloglabs.jbe.TestsDataUtils;
import fr.supraloglabs.jbe.model.dto.UserDTO;
import fr.supraloglabs.jbe.model.po.User;

/**
 * Classe des Tests d'Integration composants des objets de type {@link UserMapper}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "userMapperTest", classes = {ModelMapper.class, UserMapper.class })
@SpringBootTest
@ActiveProfiles("test")
class UserMapperTest
{

    @Autowired
    private UserMapper userMapper;

    private UserDTO dto;
    private User user;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.dto = TestsDataUtils.USER_DTO_TEST;
        this.user = TestsDataUtils.USER_TEST;
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.userMapper = null;
        this.dto = null;
        this.user = null;
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.service.mapper.UserMapper#convertToUser(fr.supraloglabs.jbe.model.dto.UserDTO)}.
     */
    @Test
    void testConvertToUser()
    {
        final User response = this.userMapper.convertToUser(this.dto);

        assertThat(response).isNotNull();
        TestsDataUtils.assertAllUsers(TestsDataUtils.USER_TEST_NO_ID, response);
    }

    @Test
    void testConvertToUser_ShouldReturnNull()
    {
        final User response = this.userMapper.convertToUser(null);

        assertThat(response).isNull();
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.service.mapper.UserMapper#convertToUserDTO(fr.supraloglabs.jbe.model.po.User)}.
     */
    @Test
    void testConvertToUserDTO()
    {
        final UserDTO response = this.userMapper.convertToUserDTO(this.user);

        assertThat(response).isNotNull();
        TestsDataUtils.assertAllUsersUserAndUserDTO(this.user, response);
    }

    @Test
    void testConvertToUserDTO_ShouldReturnNull()
    {
        final UserDTO response = this.userMapper.convertToUserDTO(null);

        assertThat(response).isNull();
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.service.mapper.UserMapper#totoUsers(java.util.Collection)}.
     */
    @Test
    void testTotoUsers()
    {
        final List<UserDTO> dtos = Lists.newArrayList();
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);

        final List<User> users = (List<User>) this.userMapper.totoUsers(dtos);
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isPositive();
        assertThat(users.size()).isEqualTo(3);
        assertThat(users).contains(TestsDataUtils.USER_TEST_NO_ID);
        TestsDataUtils.assertAllUsersUserAndUserDTO(users.get(0), dtos.get(0));
    }

    void testTotoUsers_WithNullElement()
    {
        final List<UserDTO> dtos = Lists.newArrayList();
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(null);

        final List<User> users = (List<User>) this.userMapper.totoUsers(dtos);
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isPositive();
        assertThat(users.size()).isEqualTo(2);
        assertThat(users).contains(TestsDataUtils.USER_TEST_NO_ID);
        TestsDataUtils.assertAllUsersUserAndUserDTO(users.get(0), dtos.get(0));
    }

    // @Test
    // void testTotoUsers_WithNullList()
    // {
    // final List<User> users = (List<User>) this.userMapper.totoUsers(null);
    // assertThat(users).isEmpty();
    // assertThat(users.size()).isNotPositive();
    // }

    /**
     * Test method for {@link fr.supraloglabs.jbe.service.mapper.UserMapper#toUserDtos(java.util.Collection)}.
     */
    @Test
    void testToUserDtos()
    {
        final List<User> users = Lists.newArrayList();
        users.add(this.user);
        users.add(this.user);
        users.add(this.user);

        final List<UserDTO> dtos = (List<UserDTO>) this.userMapper.toUserDtos(users);
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isPositive();
        assertThat(dtos.size()).isEqualTo(3);
        TestsDataUtils.assertAllUsersUserAndUserDTO(users.get(0), dtos.get(0));
    }

    @Test
    void testToUserDtos_WithNullElement()
    {
        final List<User> users = Lists.newArrayList();
        users.add(this.user);
        users.add(this.user);
        users.add(null);

        final List<UserDTO> dtos = (List<UserDTO>) this.userMapper.toUserDtos(users);
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isPositive();
        assertThat(dtos.size()).isEqualTo(2);
        TestsDataUtils.assertAllUsersUserAndUserDTO(users.get(0), dtos.get(0));
    }

    // @Test
    // void testToUserDtos_WithNullList()
    // {
    // final List<UserDTO> dtos = (List<UserDTO>) this.userMapper.toUserDtos(null);
    // assertThat(dtos).isEmpty();
    // assertThat(dtos.size()).isNotPositive();
    // }
}
