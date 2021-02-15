/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : UserAccountException.java
 * Date de création : 15 févr. 2021
 * Heure de création : 10:00:00
 * Package : fr.supraloglabs.jbe.error
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.error;

/**
 * * Classe d'interception des erreurs/exceptions metiers ou applicatifs. Elles ne sont pas vérifiees par le compilateur
 * (Unchecked exceptions) mais à l'éxécution.
 * 
 * @author Vincent Otchoun
 */
public class UserAccountException extends RuntimeException
{
    /**
     * Identitifnat de sérialisation/désérialisation.
     */
    private static final long serialVersionUID = -7379744353705557520L;

    /**
     * Constructeur avec en paramètre le message d'erreurs.
     * 
     * @param message le message d'erreurs.
     */
    public UserAccountException(String message)
    {
        super(message);
    }

    /**
     * Constructeur avec la cause de l'erreur..
     * 
     * @param cause la cause de l'erreur.
     */
    public UserAccountException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructeur avec la cause et le message d'erreurs.
     * 
     * @param message le message d'erreurs.
     * @param cause   la cause de l'erreur.
     */
    public UserAccountException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
