/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserServiceIT.java
 * Date de création : 17 févr. 2021
 * Heure de création : 18:22:30
 * Package : fr.supraloglabs.jbe.service.user
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import fr.supraloglabs.jbe.TestsDataUtils;
import fr.supraloglabs.jbe.config.AppRootConfig;
import fr.supraloglabs.jbe.error.AppCustomException;
import fr.supraloglabs.jbe.model.po.User;
import fr.supraloglabs.jbe.util.UserAccountUtil;

/**
 * Classe des Tests d'Intégration Système des objets de type {@link UserService}.
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "userServiceIT", classes = { AppRootConfig.class, UserService.class })
@SpringBootTest
@ActiveProfiles("test")
class UserServiceIT
{
    private static final String OBJECT_NOT_NULL = "must not be null!";

    @Autowired
    private MongoOperations mongoOps;

    @Autowired
    private UserService userService;
    private User user;
    private User user2;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.user = TestsDataUtils.USER_SERVICE_TEST;
        this.user2 = TestsDataUtils.USER_TEST_NO_ID;

        if (!this.mongoOps.collectionExists(User.class))
        {
            this.mongoOps.createCollection(User.class);
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.userService = null;
        this.mongoOps.dropCollection(User.class);
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.service.user.UserService#createUser(fr.supraloglabs.jbe.model.po.User)}.
     */
    @Test
    void testCreateUser()
    {
        final User savedUser = this.userService.createUser(this.user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotEmpty();

        final List<User> users = (List<User>) this.userService.getUsers();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void testCreateUser_user()
    {
        final User savedUser = this.userService.createUser(this.user2);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotEmpty();

        final List<User> users = (List<User>) this.userService.getUsers();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void testCreateUser_ShouldThrowException()
    {
        final Exception exception = assertThrows(AppCustomException.class, () -> {
            this.userService.createUser(null);
        });

        final String expectedMessage = OBJECT_NOT_NULL;
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
        final User savedUser = this.userService.createUser(this.user2);
        assertThat(savedUser).isNotNull();

        final String email = savedUser.getEmail();
        final List<User> userFromDB = (List<User>) this.userService.getByEmailIgnoreCase(email);

        assertThat(userFromDB).isNotEmpty();
        assertThat(userFromDB.size()).isPositive();

        TestsDataUtils.assertAllUsers(savedUser, userFromDB.get(0));
    }

    @Test
    void testGetByEmailIgnoreCase_UpperCase()
    {
        final User savedUser = this.userService.createUser(this.user2);
        assertThat(savedUser).isNotNull();

        final String email = savedUser.getEmail();
        final String upper = email.toUpperCase();
        final List<User> userFromDB = (List<User>) this.userService.getByEmailIgnoreCase(upper);

        assertThat(userFromDB).isNotEmpty();
        assertThat(userFromDB.size()).isPositive();

        TestsDataUtils.assertAllUsers(savedUser, userFromDB.get(0));
    }

    @Test
    void testGetByEmailIgnoreCase_WithEmpty()
    {
        final String email = StringUtils.EMPTY;
        final List<User> userFromDB = (List<User>) this.userService.getByEmailIgnoreCase(email);

        assertThat(userFromDB).isEmpty();
        assertThat(userFromDB.size()).isNotPositive();
    }

    @Test
    void testGetByEmailIgnoreCase_WithNull()
    {
        final String email = null;
        final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.userService.getByEmailIgnoreCase(email);
        });

        final String expectedMessage = OBJECT_NOT_NULL;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.service.user.UserService#getUserById(java.lang.String)}.
     */
    @Test
    void testGetUserById()
    {
        final User savedUser = this.userService.createUser(this.user2);
        assertThat(savedUser).isNotNull();

        final String id = savedUser.getId();

        final Optional<User> userFromDB = this.userService.getUserById(id);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUsers(savedUser, userFromDB.get());
    }

    @Test
    void testGetUserById_ShouldThrowException()
    {
        final String id = "test";
        final Exception exception = assertThrows(AppCustomException.class, () -> {
            this.userService.getUserById(id);
        });

        final String expectedMessage = UserAccountUtil.FIND_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.service.user.UserService#getAllUsersPageable(org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testGetAllUsersPageable()
    {
        final User savedUser = this.userService.createUser(this.user2);
        assertThat(savedUser).isNotNull();

        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        final Page<User> result = this.userService.getAllUsersPageable(paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(1); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(1); // La taille du contenu
    }

    @Test
    void testGetAllUsersPageable_WithNull()
    {
        final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.userService.getAllUsersPageable(null);
        });

        final String expectedMessage = OBJECT_NOT_NULL;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.service.user.UserService#getUsers()}.
     */
    @Test
    void testGetUsers()
    {
        final List<User> result = (List<User>) this.userService.getUsers();

        assertThat(result).isEmpty(); // Car on vide la collection à chaque test.
        assertThat(result.size()).isNotPositive(); // Car on vide la collection à chaque test.
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.service.user.UserService#deleteUser(java.lang.String)}.
     */
    @Test
    void testDeleteUser()
    {
        final User savedUser = this.userService.createUser(this.user2);
        assertThat(savedUser).isNotNull();
        final String id = savedUser.getId();

        final Optional<User> userFromDB = this.userService.getUserById(id);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUsers(savedUser, userFromDB.get());

        this.userService.deleteUser(id);

        final List<User> result = (List<User>) this.userService.getUsers();

        assertThat(result).isEmpty(); // Car on vide la collection à chaque test.
        assertThat(result.size()).isNotPositive(); // Car on vide la collection à chaque test.
    }

    @Test
    void testDeleteUser_ShouldThrowExceptionWithEmptyId()
    {
        final String id = StringUtils.EMPTY;
        final Exception exception = assertThrows(AppCustomException.class, () -> {
            this.userService.deleteUser(id);
        });

        final String expectedMessage = UserAccountUtil.FIND_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
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
    }

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.service.user.UserService#updateUser(java.lang.String, fr.supraloglabs.jbe.model.po.User)}.
     */
    @Test
    void testUpdateUser()
    {
        final User savedUser = this.userService.createUser(this.user2);
        assertThat(savedUser).isNotNull();
        final String id = savedUser.getId();

        //
        savedUser.setFirstName("Adjiba");
        savedUser.setLastName("Milande");
        savedUser.setEmail("milande.test@live.fr");
        savedUser.setAge(21);

        this.userService.updateUser(id, savedUser);

        assertThat(savedUser.getId()).isEqualTo(id);
        assertThat(savedUser.getFirstName()).isEqualTo("Adjiba");
        assertThat(savedUser.getLastName()).isEqualTo("Milande");
        assertThat(savedUser.getEmail()).isEqualTo("milande.test@live.fr");
    }

    @Test
    void testUpdateUser_ShouldThrowException()
    {
        final String id = "1L";
        final Exception exception = assertThrows(AppCustomException.class, () -> {
            this.userService.getUserById(id);
        });

        final String expectedMessage = UserAccountUtil.FIND_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
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
