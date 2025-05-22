# Tirage au sort - Plugin Lutece

## 📝 Présentation

Ce projet a été réalisé dans le cadre de mon stage de fin de deuxième année de **BTS SNIR** (*Systèmes Numériques Informatique et Réseaux*). Le développement a été effectué au sein du **service informatique de la Mairie de Paris**.

L’objectif du projet était de concevoir un **plugin pour Lutece** (framework open-source utilisé par les administrations) permettant d’effectuer un **tirage au sort équitable** afin de désigner une personne pour **payer le café**, tout en garantissant qu’une même personne ne soit pas tirée deux fois avant que tous les participants aient été sélectionnés.

## ⚙️ Technologies utilisées

- **Java EE**
- **Apache Tomcat**
- **Maven**
- **SQL / MariaDB**
- **Lutece** (framework de la Ville de Paris)

## 🔍 Fonctionnalités

- Interface d’administration via Lutece pour gérer les participants
- Tirage au sort aléatoire parmi une liste d’utilisateurs
- Mécanisme d’exclusion temporaire pour éviter les doublons jusqu’à rotation complète
- Persistance des tirages dans une base de données SQL
- Déploiement sur un serveur Tomcat

## 📂 Structure du projet

- `src/` : code source Java du plugin
- `webapp/` : fichiers JSP et configuration Lutece
- `Test/` : éventuels tests
- `pom.xml` : configuration Maven

## 🧑‍💻 Informations

> Stage réalisé au service informatique de la Mairie de Paris
> 
> Dans le cadre du BTS SNIR (Systèmes Numériques option Informatique et Réseaux)
> 
> Durée : du 22 mai 2023 au 1er juillet 2023 (6 semaines)
