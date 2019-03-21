ESUP-Helpdesk project

pour une installation sur une base existante : 

si besoin, renommer le répertoire src/main/resources/properties/logging.sample en src/main/resources/properties/logging et adapter la configuration
si besoin, renommer le répertoire src/main/resources/properties/config.properties.sample en src/main/resources/properties/config.properties.sample et adapter la configuration
si besoin, renommer le répertoire webapp/media.sample en webapp/media et adapter la configuration (images, css etc..)
si besoin, renommer le fichier build-commons.sample.xml en build-commons.xml
si besoin, renommer le fichier build.properties.sample en build.properties

mise à jour de la base de donnée avec lancement de la commande :
    ant upgrade

initialisation des nouvelles colonnes avec lancement des commandes en base de donnée contenues dans le fichier :
    src/main/resources/properties/dao/hibernate/updateBase.sql 

compilation et deploiement des sources avec lancement de la commande :
    ant clean compile deploy
