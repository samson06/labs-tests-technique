/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserAccountUtilTest.java
 * Date de création : 16 févr. 2021
 * Heure de création : 01:51:24
 * Package : fr.supraloglabs.jbe.util
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Classe des Tests Unitaires des objets de type {@link UserAccountUtil}
 * 
 * @author Vincent Otchoun
 */
class UserAccountUtilTest
{

    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.util.UserAccountUtil#mongoDBConectionStr(java.lang.String, java.lang.Integer, java.lang.String)}.
     */
    @Test
    void testMongoDBConectionStr()
    {
        String host = "OVIOK";
        Integer port = 12345;
        String base = "test_db";

        final String response = UserAccountUtil.mongoDBConnectionStr(host, port, base);

        assertThat(response).isEqualTo("mongodb://OVIOK:12345/test_db");
    }

    @Test
    void testMongoDBConectionStr_()
    {
        final String response = UserAccountUtil.mongoDBConnectionStr(null, null, null);

        assertThat(response).isNull();
    }
    
    /**
     * Test method for
     * {@link fr.supraloglabs.jbe.util.UserAccountUtil#mongoDBConnectionStrDefault()}.
     */
    @Test
    void testMongoDBConnectionStrDefault()
    {
        final String response = UserAccountUtil.mongoDBConnectionStrDefault();
        
        assertThat(response).isNotNull();
        assertThat(response.toString()).isEqualTo("mongodb://localhost:12345/users_db_test");
    }
}
