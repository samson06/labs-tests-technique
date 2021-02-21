/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserServiceTest.java
 * Date de création : 17 févr. 2021
 * Heure de création : 14:06:42
 * Package : fr.supraloglabs.jbe.service.user
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import fr.supraloglabs.jbe.TestsDataUtils;
import fr.supraloglabs.jbe.config.AppRootConfig;
import fr.supraloglabs.jbe.error.AppCustomException;
import fr.supraloglabs.jbe.model.po.User;
import fr.supraloglabs.jbe.repository.UserRepository;
import fr.supraloglabs.jbe.util.UserAccountUtil;

/**
 * Classe des Tests Unitaires des objets de type {@link UserService}.
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "userServiceTest", classes = { AppRootConfig.class, UserService.class })
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest
{
    @Mock
    private UserRepository userRepository;

    private UserService userService;
    private User user;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.userService = new UserService(this.userRepository);

        this.user = TestsDataUtils.USER_TEST_NO_ID;
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        //
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.service.user.UserService#createUser(fr.supraloglabs.jbe.model.po.User)}.
     */
    @DisplayName("given object to save when save object using UserRepository then object is saved")
    @Test
    void testCreateUser()
    {
        final User mockUser = this.user;
        final User userToSaved = this.user;

        BDDMockito.given(this.userRepository.save(Mockito.any(User.class))).willReturn(mockUser);
        final User savedUser = this.userService.createUser(userToSaved);

        assertThat(savedUser).isNotNull();
        TestsDataUtils.assertAllUsers(mockUser, savedUser);

        verify(this.userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    void testCreateUser_ShouldThrowException()
    {
        final Exception exception = assertThrows(AppCustomException.class, () -> {
            this.userService.createUser(null);
        });

        final String expectedMessage = UserAccountUtil.SAVE_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.service.user.UserService#getByEmailIgnoreCase(java.lang.String)}.
     */
    @Test
    void testGetByEmailIgnoreCase()
    {
        final List<User> users = Lists.newArrayList();
        users.add(this.user);

        final String email = user.getEmail();

        BDDMockito.given(this.userRepository.findByEmailIgnoreCase(Mockito.any(String.class))).willReturn(users);
        final List<User> userFromDB = (List<User>) this.userService.getByEmailIgnoreCase(email);

        assertThat(userFromDB).isNotEmpty();
        assertThat(userFromDB.size()).isPositive();

        TestsDataUtils.assertAllUsers(this.user, userFromDB.get(0));

        verify(this.userRepository, times(1)).findByEmailIgnoreCase(Mockito.any(String.class));
    }

    @Test
    void testGetByEmailIgnoreCase_UpperCase()
    {
        final List<User> users = Lists.newArrayList();
        users.add(this.user);

        final String email = user.getEmail();
        final String upper = email.toUpperCase();

        BDDMockito.given(this.userRepository.findByEmailIgnoreCase(Mockito.any(String.class))).willReturn(users);
        final List<User> userFromDB = (List<User>) this.userService.getByEmailIgnoreCase(upper);

        assertThat(userFromDB).isNotEmpty();
        assertThat(userFromDB.size()).isPositive();

        TestsDataUtils.assertAllUsers(this.user, userFromDB.get(0));

        verify(this.userRepository, times(1)).findByEmailIgnoreCase(Mockito.any(String.class));
    }

    @Test
    void testGetByEmailIgnoreCase_WithEmpty()
    {
        final String email = StringUtils.EMPTY;
        final List<User> userFromDB = (List<User>) this.userService.getByEmailIgnoreCase(email);

        assertThat(userFromDB).isEmpty();
        assertThat(userFromDB.size()).isNotPositive();

        verify(this.userRepository, times(1)).findByEmailIgnoreCase(Mockito.any(String.class));
    }

    @Test
    void testGetByEmailIgnoreCase_WithNull()
    {

        final String email = null;

        final List<User> userFromDB = (List<User>) this.userService.getByEmailIgnoreCase(email);

        assertThat(userFromDB).isEmpty();
        assertThat(userFromDB.size()).isNotPositive();

        verify(this.userRepository, times(1)).findByEmailIgnoreCase(Mockito.any(String.class));
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.service.user.UserService#getUserById(java.lang.String)}.
     */
    @Test
    void testGetUserById()
    {
        final Optional<User> optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final User user = optional.get();
        user.setId("1L");
        final String id = user.getId();

        BDDMockito.given(this.userRepository.findOneById(Mockito.any(String.class))).willReturn(optional);
        final Optional<User> userFromDB = this.userService.getUserById(id);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUsers(user, userFromDB.get());

        verify(this.userRepository, times(1)).findOneById(Mockito.any(String.class));
    }

    @Test
    void testGetUserById_ShouldThrowException()
    {
        final Optional<User> optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final User user = optional.get();
        user.setId("1L");
        final String id = user.getId();

        BDDMockito.given(this.userRepository.findOneById(Mockito.any(String.class))).willReturn(Optional.empty());
        final Exception exception = assertThrows(AppCustomException.class, () -> {
            this.userService.getUserById(id);
        });

        final String expectedMessage = UserAccountUtil.FIND_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.userRepository, times(1)).findOneById(Mockito.any(String.class));
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.service.user.UserService#getAllUsersPageable(org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testGetAllUsersPageable()
    {
        final List<User> users = Lists.newArrayList();
        users.add(this.user);
        users.add(this.user);
        users.add(this.user);

        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        final Page<User> page = new PageImpl<User>(users);

        BDDMockito.given(this.userRepository.findAll(Mockito.any(Pageable.class))).willReturn(page);

        final Page<User> result = this.userService.getAllUsersPageable(paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(3); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(3); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(3); // La taille du contenu

        verify(this.userRepository, times(1)).findAll(Mockito.any(Pageable.class));
    }

    @Test
    void testGetAllUsersPageable_WithNull()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        BDDMockito.given(this.userRepository.findAll(Mockito.any(Pageable.class))).willReturn(null);

        final Page<User> result = this.userService.getAllUsersPageable(paging);

        assertThat(result).isNull();

        verify(this.userRepository, times(1)).findAll(Mockito.any(Pageable.class));
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.service.user.UserService#getUsers()}.
     */
    @Test
    void testGetUsers()
    {
        final List<User> users = Lists.newArrayList();
        users.add(this.user);
        users.add(this.user);
        users.add(this.user);

        BDDMockito.given(this.userRepository.findAll()).willReturn(users);

        final List<User> result = (List<User>) this.userService.getUsers();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(3);
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.service.user.UserService#deleteUser(java.lang.String)}.
     */
    @Test
    void testDeleteUser()
    {
        final Optional<User> optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final User user = optional.get();
        user.setId("1L");
        final String id = user.getId();

        BDDMockito.given(this.userRepository.findOneById(Mockito.any(String.class))).willReturn(optional);
        final Optional<User> userFromDB = this.userService.getUserById(id);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUsers(user, userFromDB.get());

        this.userService.deleteUser(id);

        verify(this.userRepository, times(2)).findOneById(Mockito.any(String.class));
        verify(this.userRepository, times(1)).delete(Mockito.any(User.class));
    }

    @Test
    void testDeleteUser_ShouldThrowExceptionWithEmptyId()
    {
        final Optional<User> optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final User user = optional.get();
        user.setId("1L");
        final String id = user.getId();

        BDDMockito.given(this.userRepository.findOneById(Mockito.any(String.class))).willReturn(Optional.empty());
        final Exception exception = assertThrows(AppCustomException.class, () -> {
            this.userService.deleteUser(id);
        });

        final String expectedMessage = UserAccountUtil.FIND_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.userRepository, times(1)).findOneById(Mockito.any(String.class));
    }

    @Test
    void testDeleteUser_ShouldThrowExceptionWithNull()
    {
        final Exception exception = assertThrows(AppCustomException.class, () -> {
            this.userService.deleteUser(null);
        });

        final String expectedMessage = UserAccountUtil.FIND_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.userRepository, times(1)).findOneById(Mockito.any(String.class));
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.service.user.UserService#updateUser(java.lang.String, fr.supraloglabs.jbe.model.po.User)}.
     */
    @Test
    void testUpdateUser()
    {
        final Optional<User> optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final User userToUpdated = optional.get();
        userToUpdated.setId("1L");
        userToUpdated.setEmail("update.test@test.com");

        final String id = user.getId();
        BDDMockito.given(this.userRepository.save(Mockito.any(User.class))).willReturn(userToUpdated);
        BDDMockito.given(this.userRepository.findOneById(Mockito.any(String.class))).willReturn(Optional.of(userToUpdated));

        final Optional<User> userFromDB = this.userService.getUserById(id);
        assertThat(userFromDB).isPresent();

        final User result = this.userService.updateUser(userFromDB.get().getId(), userToUpdated);

        assertThat(result.getId()).isEqualTo("1L");
        assertThat(result.getEmail()).isEqualTo("update.test@test.com");

        verify(this.userRepository, times(2)).findOneById(Mockito.any(String.class));
    }

    @Test
    void testUpdateUser_ShouldThrowException()
    {
        final Optional<User> optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final User userToUpdated = optional.get();
        userToUpdated.setId("1L");
        userToUpdated.setEmail("update.test@test.com");

        final String id = user.getId();
        BDDMockito.given(this.userRepository.save(Mockito.any(User.class))).willReturn(userToUpdated);
        BDDMockito.given(this.userRepository.findOneById(Mockito.any(String.class))).willReturn(Optional.empty());

        final Exception exception = assertThrows(AppCustomException.class, () -> {
            this.userService.getUserById(id);
        });

        final String expectedMessage = UserAccountUtil.FIND_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.userRepository, times(1)).findOneById(Mockito.any(String.class));
    }

    @Test
    void testUpdateUser_ShouldThrowExceptionWuithNull()
    {
        final Exception exception = assertThrows(AppCustomException.class, () -> {
            this.userService.updateUser(null, null);
        });

        final String expectedMessage = UserAccountUtil.FIND_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }
}
