/*
 * ----------------------------------------------
 * Projet ou Module : labs-tests-technique
 * Nom de la classe : LoggingAspectConfig.java
 * Date de création : 21 févr. 2021
 * Heure de création : 18:37:17
 * Package : fr.supraloglabs.jbe.config.aop
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.supraloglabs.jbe.config.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration AOP pour la séparation des préoccupations traversales (journalisation dans les couches applicatives).
 * 
 * @author Vincent Otchoun
 */
@Configuration
@Aspect
@Slf4j
public class LoggingAspectConfig
{

    // private final Environment env;

    /**
     * Constructeur avec paramètre pour injection du bean.
     * 
     * @param env Interface représentant l'environnement dans lequel l'application s'exécute.
     */
    // @Autowired
    // public LoggingAspectConfig(final Environment pEnv)
    // {
    // this.env = pEnv;
    // }

    // Défintion des Pointcut : définir quand un appel à une méthode doit être intercepté.
    @Pointcut("execution(* fr.supraloglabs.jbe.repository.*.*(..))")
    public void repoLayerExecution()
    {
        //
    }

    @Pointcut("execution(* fr.supraloglabs.jbe.service.*.*(..))")
    public void businessLayerExecution()
    {
        //
    }

    @Pointcut("execution(* fr.supraloglabs.jbe.controller.*.*(..))")
    public void endpointsLayerExecution()
    {
        //
    }

    @Pointcut("execution(* fr.supraloglabs.jbe.error.*.*(..))")
    public void errorLayerExecution()
    {
        //
    }

    @Pointcut("execution(* fr.supraloglabs.jbe.config.*.*(..))")
    public void configLayerExecution()
    {
        //
    }

    /**
     * Advice qui enregistre les méthodes lançant des exceptions.
     * 
     * @param joinPoint le point de jonction.
     * @param e         exception ou erreur survenue lors de l'exécution de l'application
     */
    @AfterThrowing(pointcut = " endpointsLayerExecution() || businessLayerExecution() || errorLayerExecution() || configLayerExecution() || repoLayerExecution()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e)
    {
        final String type = joinPoint.getSignature().getDeclaringTypeName();
        final String nomMethode = joinPoint.getSignature().getName();

        // if (this.env != null && this.env.acceptsProfiles(Profiles.of(UserAccountUtil.ENV)))
        // {
        log.error("Erreur dans {}.{}() avec la cause = \'{}\' et le message = \'{}\'", type, nomMethode, e.getCause() != null ? e.getCause() : "NULL",
        e.getMessage(), e);
        // }
        // else
        // {
        // log.error("Erreur dans {}.{}() avec la cause = {}", type, nomMethode, e.getCause() != null ? e.getCause() : "NULL");
        // }
    }

    /**
     * Advice qui enregistre lorsqu'une méthode est entrée et sortie avec calcul de sa durée d'exécution.
     * 
     * @param joinPoint le point de jonction.
     * @return le resultat ou l'execption/erreur survenue.
     * @throws Throwable exception/erreur lorsque survient une erreur.
     */
    @Around("endpointsLayerExecution() || businessLayerExecution() || errorLayerExecution() || configLayerExecution() || repoLayerExecution()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        final String typeClasse = joinPoint.getSignature().getDeclaringTypeName();
        final String nomMethode = joinPoint.getSignature().getName();
        final Object[] argsMethode = joinPoint.getArgs();

        log.info("Entrée dans la méthode : {}.{}() argument[s] = {}", typeClasse, nomMethode, Arrays.toString(argsMethode));

        final StopWatch clock = new StopWatch(getClass().getName());

        try
        {
            clock.start(joinPoint.toString());
            final Object result = joinPoint.proceed();

            log.info("Sortie de la méthode : {}.{}() avec le résultat  = {}", typeClasse, nomMethode, result);
            return result;
        }
        catch (IllegalArgumentException e)
        {
            log.error("Illegal argument: {} dans la méthode {}.{}()", Arrays.toString(argsMethode), typeClasse, nomMethode);
            throw e;
        }
        finally
        {
            clock.stop();
            log.info("Le Temps d'execution : {}", clock.prettyPrint());
        }
    }

}
