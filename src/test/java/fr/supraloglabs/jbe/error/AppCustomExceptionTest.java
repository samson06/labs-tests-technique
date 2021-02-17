/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserAccountExceptionTest.java
 * Date de création : 15 févr. 2021
 * Heure de création : 10:04:24
 * Package : fr.supraloglabs.jbe.error
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */	
package fr.supraloglabs.jbe.error;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe des Tests Unitaires des objets de type {@link AppCustomException}
 * @author Vincent Otchoun
 *
 */
class AppCustomExceptionTest
{
    
    private AppCustomException error;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.error = null;
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.error.AppCustomException#UserAccountException(java.lang.String)}.
     */
    @Test
    void testUserAccountExceptionString()
    {
        final String message = "Message Erreur Exception";
        this.error = new AppCustomException(message);

        //
        assertThat(this.error.getCause()).isNull();
        assertThat(this.error.getMessage()).isEqualToIgnoringCase(message);
        assertThat(this.error.getLocalizedMessage()).isNotNull();
        assertThat(this.error.getStackTrace()).isNotNull();
    }
    
    @Test
    void testUserAccountExceptionString_WithNull()
    {
        this.error = new AppCustomException((String) null);

        // AssertJ assertion's
        assertThat(this.error.getCause()).isNull();
        assertThat(this.error.getMessage()).isNull();
        assertThat(this.error.getLocalizedMessage()).isNull();
        assertThat(this.error.getStackTrace()).isNotNull();
    }
    

    /**
     * Test method for {@link fr.supraloglabs.jbe.error.AppCustomException#UserAccountException(java.lang.Throwable)}.
     */
    @Test
    void testUserAccountExceptionThrowable()
    {
        final String message = "Message Erreur Exception";
        final Throwable throwable = new Throwable(message);
        
        this.error = new AppCustomException(throwable);
        
        // AssertJ assertion's
        assertThat(this.error.getCause()).isNotNull();
        assertThat(this.error.getCause().toString()).contains("Message"); 
        assertThat(this.error.getMessage()).isNotNull();
        assertThat(this.error.getLocalizedMessage()).isNotNull();
        assertThat(this.error.getStackTrace()).isNotNull();
    }
    
    @Test
    void testUserAccountExceptionThrowable_WithNull()
    {
        final Throwable throwable = new Throwable((String) null);
        
        this.error = new AppCustomException(throwable);
        
        // AssertJ assertion's
        assertThat(this.error.getCause()).isExactlyInstanceOf(Throwable.class);
        assertThat(this.error.getMessage()).isNotNull();
        assertThat(this.error.getLocalizedMessage()).isNotNull();
        assertThat(this.error.getStackTrace()).isNotNull();
    }

    /**
     * Test method for {@link fr.supraloglabs.jbe.error.AppCustomException#UserAccountException(java.lang.String, java.lang.Throwable)}.
     */
    @Test
    void testUserAccountExceptionStringThrowable()
    {
        final String message = "Message Erreur Exception";
        final Throwable throwable = new Throwable(message);
        
        this.error = new AppCustomException(message, throwable);
        
        // AssertJ assertion's
        assertThat(this.error.getCause()).isNotNull();
        assertThat(this.error.getCause().toString()).contains("Message"); 
        assertThat(this.error.getMessage()).isNotNull();
        assertThat(this.error.getLocalizedMessage()).isNotNull();
        assertThat(this.error.getStackTrace()).isNotNull();
    }
    
    @Test
    void testUserAccountExceptionStringThrowable_WithNull()
    {
        final Throwable throwable = new Throwable((String) null, null);
        
        this.error = new AppCustomException(throwable);
        
        // AssertJ assertion's
        assertThat(this.error.getCause()).isExactlyInstanceOf(Throwable.class);
        assertThat(this.error.getMessage()).isNotNull();
        assertThat(this.error.getLocalizedMessage()).isNotNull();
        assertThat(this.error.getStackTrace()).isNotNull();
    }
}
