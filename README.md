# REST Services

![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg?branch=master)
![GitHub language count](https://img.shields.io/github/languages/count/samson06/labs-tests-technique) 
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/samson06/labs-tests-technique)
![GitHub repo size](https://img.shields.io/github/repo-size/samson06/labs-tests-technique)
![GitHub last commit](https://img.shields.io/github/last-commit/samson06/labs-tests-technique)

## A Propos de l'application
C'est une application basée sur une architecture `REST API` (`service RESTFul`) exposant ses fonctionnalités au travers de deux `API`. Elle est écrite en `Java` 
avec pour socle le Framework `Spring` plus précisement `Spring Boot` qui est des variante de l'écosystème`Spring`. Elle embarque aussi d'autres technos supplémentaires pour l'impémentation des besoins exprimés.

## Documentation
Cette application est livrée avec des documents situés dans le dossier : `labs-tests-technique/docs/`
- `L'expression des besoins` fournie par le donneur d'ordre  : Test Technique - JAVA BACK END.pdf
- Le document des `Spécifications Techniques Détaillées` : STD_Test_Technique.pdf 

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

C'est un projet `Maven` avec `Spring Boot` donc basé sur le langage `Java` : 
- `EA` (Entreprise Architect) pour la fourniture des éléments de modélisation/conception des spécifications techniques fournies.
- `Java 8` est la version du langage utilisé pour le code source et cible pour l'environnement d'exécution ou de production.
- `MongoDB`, configurations pour les accès aux données en base. Elle facilite également les réalisations de TI (`_Tests d'Intégration_` : composants ou système).
- l'interface `MongoRepository ` pour les concepts ORM et DAO.
- `Lombok` pour générer du code couramment utilisé et faciliter le maintien du code source propre, simplifier l'écriture des classes.
- `JUnit 5` pour l'écriture des codes sources des classes des Tests Unitaires et/ou d'Intégration.
- `SonarLint` intégré dans l'IDE (_STS_) pour `analyser la qualité du code` livré, poussé dans le `repository` (_bonnes pratiques de développement_).
- `JaCoCo` pour la production et fourniture des rapports de couverture du code source par les différents tests réalisés.
- `Model Mapper` pour la conversion des objets : objets persistants vers `DTO` (Data Transfert Object) et vice versa.

## Fonctionnalités
Cette application fournit des points de terminaison HTTP et des outils pour les éléments suivants : 

|Verbe HTTP|URL|Description|Status Codes|
|---|---|---|---|
|`POST`|_http://localhost:${server.port}/api-users/user/register/_|Enregistrer/Sauvegarder dans le SI les informations d'un utilisateur|<ul><li>`200 OK` si succès</li><li>`4XX ou 5XX` si erreur survenue</li></ul>|
|`GET`|_http://localhost:${server.port}/api-users/user/search/{id}_|Rechercher et afficher les détails d'un utilisateur enregistré dans le SI|<ul><li>`200 OK` si utilisateur existe</li><li>`4XX ou 5XX` si erreur survenue</li></ul>|

## Configurations
TODO

## Exécution
TODO

## Tests

### Types de Tests
- `Tests unitaires` : pas seulement pour un effet de test immédiat du code, mais également permettre d'effectuer des tests de non-régression.
- `Tests d'intégration` : assurer que le comportement de l'application est toujours aussi conforme, au fur et à mesure de l'assemblage des unités de code. Nous couvirons les deux types à savoir :
	- `Tests d'intégration composants` : vérifier que les unités de code fonctionnent corectement ensemble,c'est-à-dire de manière isolée : `Est-ce que les classes testées unitairement fonctionnent vraiment bien ensemble ?`
	- `Tests d'intégration système` : vérifier le bon fonctionnement de plusieurs unités de code au sein d'une configuration d'application (liens avec des composants extérieurs comme une base de données par exemple) : `Comment pouvons-nous rapidement tester que notre application en fonctionnement collaborerait avec le monde extérieur ?`
- `Tests fonctionnels` : Pas d'interfaces utlisateur dans le cadre de cete application pour effetuer les tests `end-to-end`. Mais les fonctionnalités exposées par les API seront testées au travers de l'outil `Postman`.

### Outils de Tests 
La partie `test` de l’écosystème `Spring` (Framework de base de l’application) plus précisément sa composante : `spring-boot-starter-test`, avec ses apports : `spring-test, spring-boot-test, spring-boot-test-autoconfigure`, 
fournit des outils permettant la réalisation des types de tests cités ci-dessus.
Le tableau ci-dessous dresse une liste des outils disponibles pour la réalisation des Tests :

|Outil|Description|
|---|---|
|`Mockito/BDDMockito`|_pour les mocks /Style d'écriture des tests de développement (Behavior Driven Development)  piloté par le comportement, il utilise : //given //when //then_|
|`JUnit 5`|_pour l'écriture des classes des Tests Unitaires et d'intégration_|
|`Assert-J`|_pour les assertions_|
|`Postman`|_pour tester les fonctionnalités exposées par les API_|
|`JaCoCo`|_Plugin maven (avec les plugin surefire et failsafe) pour produire/fournir les rapports de couverture de codes_|

### Rapports de couverture de codes
Comme mentionné ci-dessus la couverture de codes par les tests mis en place et exécutés, est mesurée et fournie par `JaCoCo`. L'image ci-dessous fournit la couverture du code de l'application à l'exception des objets de couche de modèle de modèles de données.

La copie d'écran ci-dessous fournit l'image de la couverture de codes de l'application.
TODO
