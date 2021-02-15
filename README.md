# REST Services

![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg?branch=master)
![GitHub language count](https://img.shields.io/github/languages/count/samson06/labs-tests-technique) 
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/samson06/labs-tests-technique)
![GitHub repo size](https://img.shields.io/github/repo-size/samson06/labs-tests-technique)
![GitHub last commit](https://img.shields.io/github/last-commit/samson06/labs-tests-technique)

## A Propos de l'application
C'est une application basée sur une architecture de type REST API (`service RESTFul`) écrit en **Java** avec le Framework **_Spring_** plus précisement **Spring Boot**. Elle embarque aussi d'autres technos supplémentaires
pour l'impémentation des besoins exprimés.

## Stack Technique
Une liste non exhaustive des technos embarquées pour le développement de cette application est présentée ci-dessous :

![](https://img.shields.io/badge/Java_8-✓-blue.svg)
![](https://img.shields.io/badge/Maven_3-✓-blue.svg)
![](https://img.shields.io/badge/Spring_Boot_2-✓-blue.svg)
![](https://img.shields.io/badge/MongoDB-✓-blue.svg)
![](https://img.shields.io/badge/Junit_5-✓-blue.svg)
![](https://img.shields.io/badge/Model_Mapper-✓-blue.svg)
![](https://img.shields.io/badge/Lombok-✓-blue.svg)

## Expression de besoins
L'expression des besoins est consignée dans le document situé sous : labs-tests-technique/docs/Test Technique - JAVA BACK END.pdf

## Fonctionnalités
Cette application fournit des points de terminaison HTTP et des outils pour les éléments suivants : 

|HTTP Verbe|URL|Description|Status Codes|
|---|---|---|---|
|`POST`|_http://localhost:${server.port}/api-users/user/register/_|Enregistrer/Sauvegarder dans le SI les informations d'un utilisateur|<ul><li>`200 OK` si la sauvagarde a réussi</li><li>`40X ou 50X` si erreur survenue lors de la persistance</li></ul>|
|`GET`|_http://localhost:${server.port}/api-users/user/search/{id}_|Rechercher et afficher les détails d'un utilisateur enregistré dans le SI|<ul><li>`200 OK` si utilisateur existe</li><li>`40X ou 50X` si erreur survenue lors de la recherche</li></ul>|


## Spécifications Techniques
TODO

## Configurations
TODO

## Exécution
TODO

## Test
TODO