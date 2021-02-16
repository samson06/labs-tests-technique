# REST Services

![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg?branch=master)
![GitHub language count](https://img.shields.io/github/languages/count/samson06/labs-tests-technique) 
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/samson06/labs-tests-technique)
![GitHub repo size](https://img.shields.io/github/repo-size/samson06/labs-tests-technique)
![GitHub last commit](https://img.shields.io/github/last-commit/samson06/labs-tests-technique)

## A Propos de l'application
C'est une application basée sur une architecture de type **REST** (`service RESTFul`) exposant ses fonctionnalités au travers d'**API**, et écrits en **Java** 
avec le Framework **_Spring_** plus précisement **Spring Boot**. Elle embarque aussi d'autres technos supplémentaires pour l'impémentation des besoins exprimés.

## Documentation
Cette application est livrée avec les documents situés sous le dossier : `labs-tests-technique/docs/`
- **L'expression des besoins** fourni par le donneur d'ordre  : Test Technique - JAVA BACK END.pdf
- Le documents des **spécifications techniques détaillées** : STD_Test_Technique.pdf 

## Stack Technique
Une liste non exhaustive des technos embarquées pour le développement de cette application est présentée ci-dessous :

![](https://img.shields.io/badge/Java_8-✓-blue.svg)
![](https://img.shields.io/badge/Maven_3-✓-blue.svg)
![](https://img.shields.io/badge/Spring_Boot_2-✓-blue.svg)
![](https://img.shields.io/badge/MongoDB-✓-blue.svg)
![](https://img.shields.io/badge/Junit_5-✓-blue.svg)
![](https://img.shields.io/badge/Model_Mapper-✓-blue.svg)
![](https://img.shields.io/badge/Lombok-✓-blue.svg)
![](https://img.shields.io/badge/JaCoCo-✓-blue.svg)

- C'est un projet `Maven` avec `Spring Boot` donc basé sur le langage **Java** : 
- `EA` (Entreprise Architect) pour la fourniture des éléments de modélisation/conception des spécifications techniques fournies.
- `Java 8` est utilisé pour la compilation du code source et cible pour l'environnement d'exécution ou de production.
- `MongoDB`, configurations base de données pour les accès aux données. La configuration permet de fonctionner aussi bien avec une source de données interne (embarquée) que externe.
Elle ficilite également les réalsations de TI (_Tests d'Intégration_: composants ou système).
- l'interface `MongoRepository ` pour les concepts ORM et DAO.
- `Lombok` bibliothèque Java pour générer du code couramment utilisé et faciliter le maintien du code source propre,  simplifier l'écriture des classes métiers (beans).
- `JUnit 5` pour l'écriture des codes sources des classes unitaires et d'intégration.
- `SonarLint` intégré dans l'IDE (_STS_) pour `analyser la qualité du code` livré, poussé dans le `repository` (_bonnes pratiques de développement_).
- `JaCoCo` pour la production et foruniture des rapports de couverture de codes.

## Fonctionnalités
Cette application fournit des points de terminaison HTTP et des outils pour les éléments suivants : 

|HTTP Verbe|URL|Description|Status Codes|
|---|---|---|---|
|`POST`|_http://localhost:${server.port}/api-users/user/register/_|Enregistrer/Sauvegarder dans le SI les informations d'un utilisateur|<ul><li>`200 OK` si la sauvagarde a réussi</li><li>`40X ou 50X` si erreur survenue lors de la persistance</li></ul>|
|`GET`|_http://localhost:${server.port}/api-users/user/search/{id}_|Rechercher et afficher les détails d'un utilisateur enregistré dans le SI|<ul><li>`200 OK` si utilisateur existe</li><li>`40X ou 50X` si erreur survenue lors de la recherche</li></ul>|

## Configurations
TODO

## Exécution
TODO

## Tests

### Types de Tests
- **Tests unitaires** : pas seulement pour un effet de test immédiat du code, mais également permettre d'effectuer des tests de non-régression lors de modifications qui interviendront inévitablement durant la vie de l'application.
- **Tests d'intégration** : assurer que le comportement de l'application est toujours aussi conforme, au fur et à mesure de l'assemblage des unités de code. Nous couvirons les deux types à savoir :
	- **_Tests d'intégration composants_** : vérifier que les unités de code fonctionnent corectement ensemble, dans un environnement de test assez proche du test unitaire, c'est-à-dire de manière isolée,
sans lien avec des composants extérieurs et ne permettant pas le démarrage d'une vraie application. Ce type de test répond à la question : `Est-ce que les classes testées unitairement fonctionnent vraiment bien ensemble ?`
	- **_Tests d'intégration système_** : vérifier le bon fonctionnement de plusieurs unités de code au sein d'une configuration d'application, avec éventuellement des liens avec des composants extérieurs
comme une base de données, des fichiers, ou des API en réseau. Ce type de test répond à la question : `Comment pouvons-nous rapidement tester que notre application en fonctionnement collaborerait avec le monde extérieur ?`
- **Tests fonctionnels** :  partent de l'interface utilisateur pour obtenir un résultat selon un scénario prédéfini. Ils imitent l'utilisateur final de l'application. Un démarrage complet de l'application est donc nécessaire.
Ce type de test répond à la question : `Comment pouvons-nous vérifier qu'un utilisateur final utilisera une application conforme et cohérente de bout en bout ?`

### Outils de Tests 
La partie `test` de l’écosystème `Spring` (Framework de base de l’application) plus précisément sa composante : `spring-boot-starter-test`, avec ses apports : `spring-test, spring-boot-test, spring-boot-test-autoconfigure`, 
fournit des outils permettant la réalisation des types de tests cités ci-dessus.
Le tableau ci-dessous dresse une liste des outils disponibles pour la réalisation des Tests :

|Outli|Description|
|---|---|
|`Mockito`|_pour les mocks_|
|`JUnit 5`|_pour l'écriture des classes des Tests Unitaires et d'intégration_|
|`Assert-J`|_pour les assertions_|
|`Postman`|_pour tester les fonctionnalités exposées par les API_|
|`JaCoCo`|Plugin maven (avec les plugin surefire et failsafe) pour produire/fournir les rapports de couverture de codes_|

### Rapports de couverture de codes
Comme mentionné ci-dessus, la couverture de codes par les tests est mesurée et fournie par `JaCoCo`. L'image ci-dessous fournit la couverture du code de l'application à l'exception des objets de couche de modèle (objets métiers).

La copie d'écran ci-dessous fournit l'image de la couverture de codes de l'application.
TODO
