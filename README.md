ESUP-Helpdesk project
==

Cette branche AMU a pour objectif de permettre aux établissements utilisant une ancienne version de esup-helpdesk de corriger quelques bugs et apporter de nouvelles fonctionnalités sans toutefois modifier l'interface graphique habituelle. Si vous souhaitez également apporter un peu de fraîcheur à l'interface, tournez-vous vers la branche AMU_IHM.


Mise à jour d'une installation existante
-

- si besoin, renommer le répertoire src/main/resources/properties/config.properties.sample en src/main/resources/properties/config.properties et adapter la configuration 
- OU copier votre ancien fichier config.properties dans src/main/resources/properties/ et ajouter les paramètres suivants :
  - ticketCommentModification=false
  - conf.inviteManagerMoveTicket=true
  - check.visi.cat.virtual=false
  - auth.tryConvertMaillToCasUser=false
  - auth.mailToConvertPattern=
- si besoin, adapter la configuration (images, css etc..) dans webapp/media 
- adapter build.properties ou recopier votre ancien fichier

mise à jour de la base de donnée avec lancement de la commande : ant upgrade

initialisation des nouvelles colonnes avec lancement des commandes en base de donnée contenues dans le fichier : src/main/resources/properties/dao/hibernate/updateBase-v3tov4.sql

compilation et deploiement des sources avec lancement de la commande : ant clean deploy

Vous pouvez retrouver cette procédure (plus détaillée) sur la [https://www.esup-portail.org/wiki/x/B4CIKg](page dédiée dans le wiki ESUP Helpdesk).
