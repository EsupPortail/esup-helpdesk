# ESUP-Helpdesk project

Cette nouvelle version du helpdesk a pour objectif de corriger quelques bugs et d'apporter un peu de fraicheur à l'application. Elle ne corrige pas certains bugs majeurs qui nécessiteraient une refonte du code (comme l'impossibilité d'ouvrir 2 pages dans 2 onglets)

Retrouver davantage d'information sur la page https://www.esup-portail.org/wiki/display/PROJHELPDESK/Esup-helpdesk

## Pour une installation sur une base existante :

- si besoin, renommer le répertoire src/main/resources/properties/logging.sample en src/main/resources/properties/logging et adapter la configuration 
- si besoin, renommer le répertoire src/main/resources/properties/config.properties.sample en src/main/resources/properties/config.properties.sample et adapter la configuration 
- si besoin, renommer le répertoire webapp/media.sample en webapp/media et adapter la configuration (images, css etc..) 
- si besoin, renommer le fichier build-commons.sample.xml en build-commons.xml si besoin, renommer le fichier build.properties.sample en build.properties

Mise à jour de la base de donnée avec lancement de la commande : 

    ant upgrade

Initialisation des nouvelles colonnes grâce au lancement des commandes sql contenues dans le fichier : src/main/resources/properties/dao/hibernate/updateBase.sql

Compilation et deploiement des sources avec lancement de la commande : 

    ant clean compile deploy
