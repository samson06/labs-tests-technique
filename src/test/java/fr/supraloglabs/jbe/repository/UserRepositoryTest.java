/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserRepositoryTest.java
 * Date de création : 17 févr. 2021
 * Heure de création : 10:26:01
 * Package : fr.supraloglabs.jbe.repository
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import fr.supraloglabs.jbe.TestsDataUtils;
import fr.supraloglabs.jbe.config.AppRootConfig;
import fr.supraloglabs.jbe.model.po.User;
import fr.supraloglabs.jbe.util.UserAccountUtil;

/**
 * Classe des Tests d'intégration des objets de type {@link UserRepository}.
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(name = "userRepositoryTest", classes = { AppRootConfig.class })
@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoOperations mongoOps;

    private User user;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.user = TestsDataUtils.USER_TEST_NO_ID;

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
        this.mongoOps.dropCollection(User.class);
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.repository.UserRepository#findOneById(java.lang.String)}.
     */
    @Test
    void testFindOneById()
    {
        final User savedUser = this.userRepository.save(this.user);
        assertThat(savedUser).isNotNull();
        final String id = savedUser.getId();
        final Optional<User> optional = this.userRepository.findOneById(id);

        assertThat(optional).isPresent();
        final User user = optional.get();

        TestsDataUtils.assertAllUsers(savedUser, user);
    }

    @Test
    void testFindOneById_WithEmpty()
    {
        final Optional<User> optional = this.userRepository.findOneById(StringUtils.EMPTY);

        assertThat(optional).isNotPresent();
    }

    @Test
    void testFindOneById_WithNullId()
    {
        final Optional<User> optional = this.userRepository.findOneById(null);

        assertThat(optional).isNotPresent();
    }

    @Test
    void testSave_ControleAge()
    {
        //
        final User userAgeControl = TestsDataUtils.USER_AGE;

        final Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            this.userRepository.save(userAgeControl);
        });

        final String expectedMessage = UserAccountUtil.AGE_MIN_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.repository.UserRepository#findByEmailIgnoreCase(java.lang.String)}.
     */
    @Test
    void testFindOneByEmailIgnoreCase()
    {
        final User savedUser = this.userRepository.save(this.user);
        assertThat(savedUser).isNotNull();

        final String email = savedUser.getEmail();
        List<User> optional = this.userRepository.findByEmailIgnoreCase(email);
        assertThat(optional.size()).isPositive();
        final User user = optional.get(0);

        assertThat(user.getEmail()).isEqualToIgnoringCase(this.user.getEmail());
        TestsDataUtils.assertAllUsers(savedUser, user);
    }

    @Test
    void testFindOneByEmailIgnoreCase_UpperCase()
    {
        final User savedUser = this.userRepository.save(this.user);

        assertThat(savedUser).isNotNull();

        final String email = savedUser.getEmail();
        final String upper = email.toUpperCase();
        List<User> optional = this.userRepository.findByEmailIgnoreCase(upper);
        assertThat(optional.size()).isPositive();
        final User user = optional.get(0);

        assertThat(user.getEmail()).isEqualToIgnoringCase(this.user.getEmail());
        TestsDataUtils.assertAllUsers(savedUser, user);
    }

    @Test
    void testGetByEmailIgnoreCase_WithEmpty()
    {
        List<User> optional = this.userRepository.findByEmailIgnoreCase(StringUtils.EMPTY);

        assertThat(optional).isEmpty();
    }

    @Test
    void testFindOneByEmailIgnoreCase_WithBadEmail()
    {
        final String email = "test";
        List<User> optional = this.userRepository.findByEmailIgnoreCase(email);

        assertThat(optional).isEmpty();
    }

    @Test
    void testFindOneByEmailIgnoreCase_ShouldThrowExceptionWithNullEmail()
    {
        final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.userRepository.findByEmailIgnoreCase(null);
        });

        final String expectedMessage = "not be null!";
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }
}
