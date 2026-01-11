========================================================================
PROJET DE PROGRAMMATION ORIENTÉE OBJET (JAVA) - GESTION DE BIBLIOTHÈQUE
========================================================================

AUTEURS (TRINÔME) :
1. Mamadou NDOYE P321706
2. Cheikhou MARA P31
3. Hamdiatou DIA

Année Universitaire : 2024-2025
Niveau : Licence 3 Informatique/INFO
Date de remise : 12 Janvier 2026

------------------------------------------------------------------------
1. DESCRIPTION DU PROJET
------------------------------------------------------------------------
Application Java complète avec interface graphique (JavaFX) pour la gestion 
d'une bibliothèque universitaire. Le projet respecte l'architecture 
DAO/Service/UI et les contraintes techniques imposées (Collections Java 
exclusives, pas de tableaux statiques).

------------------------------------------------------------------------
2. PRÉREQUIS TECHNIQUES
------------------------------------------------------------------------
- Java Development Kit (JDK) 11 ou supérieur.
- Serveur de base de données MySQL (Workbench).
- mysql-connector-j.jar (Pilote JDBC pour MySQL).
- OpenPDF (com.lowagie) ou iText (pour l'export PDF).

------------------------------------------------------------------------
3. INSTALLATION ET CONFIGURATION
------------------------------------------------------------------------
A. BASE DE DONNÉES
   1. Ouvrez votre gestionnaire SQL (MySQL Workbench).
   2. Importez/Exécutez le script fourni: "script.sql".
   3. Cela créera la base de données nommée "biblio" avec des données de test.

B. CONFIGURATION JAVA
   1. Vérifiez le fichier src/dao/ConnexionBD.java.
   2. Assurez-vous que l'URL pointe vers "biblio".
   3. Ajustez le USER et PASSWORD selon votre installation locale:
      - Workbench : user="root", password="root".

------------------------------------------------------------------------
4. LANCEMENT
------------------------------------------------------------------------
Classe principale: ui.MainFrame
Identifiants de connexion (Admin):
- Login: admin
- Mot de passe: admin

------------------------------------------------------------------------
5. FONCTIONNALITÉS IMPLÉMENTÉES 
------------------------------------------------------------------------

[MODULE 1: UTILISATEURS]
- Ajout, Modification, Suppression d'utilisateurs.
- Recherche par nom ou matricule.
- Distinction automatique Étudiant (max 3 emprunts) / Enseignant (max 5).

[MODULE 2: LIVRES]
- Gestion complète du catalogue (CRUD).
- Gestion automatique du stock (Disponible/Total).
- Recherche par titre, auteur ou ISBN.

[MODULE 3: EMPRUNTS]
- Enregistrement d'un emprunt avec vérification des règles métier (quota, stock, retards).
- Retour d'emprunt avec calcul automatique des pénalités (jours de retard).
- Historique complet avec filtres (En cours, Retard, Clôturé).

[MODULE OPTIONNEL: STATISTIQUES]
- Tableau de bord (Dashboard) dynamique avec graphiques animés.
- Indicateurs clés (KPI) : Livres, Users, Emprunts, Retards.
- Liste spécifique des emprunts en retard.

[FONCTIONNALITÉS BONUS]
- Interface Graphique (JavaFX avec CSS, animations, dégradés).
- Authentification (Login sécurisé).
- Système de Pénalités (Gestion des paiements).
- Réservation de livres (Possible uniquement si stock = 0).
- Notifications (Alertes visuelles pour les retards).
- Export des données en PDF,CSV ou TXT.

------------------------------------------------------------------------
6. GUIDE DE TEST RAPIDE (POUR LA CORRECTION)
------------------------------------------------------------------------
Afin de vérifier rapidement les fonctionnalités complexes (Stock, Pénalités, Réservations), 
nous vous suggérons de suivre ce parcours avec les données du script "script.sql" :

1. TEST DU STOCK & RÉSERVATION :
   - Tentez d'emprunter le livre "Design Patterns" (ID Livre : 13).
   - Résultat attendu : Erreur "Livre indisponible" (car stock = 0).
   - Tentez ensuite de RÉSERVER ce même livre (ID 13).
   - Résultat attendu : Succès "Réservation enregistrée".

2. TEST DU CIRCUIT RETARD & PÉNALITÉ :
   - Allez dans l'onglet "Retours" et saisissez l'ID Emprunt : 5.
   - Résultat attendu : Une alerte orange indiquant le montant de la pénalité.
   - Validez le retour.
   - Allez ensuite dans l'onglet "Pénalités", saisissez l'ID 5 et cliquez sur "Payer".
   - Résultat attendu : Confirmation verte "Pénalité réglée".