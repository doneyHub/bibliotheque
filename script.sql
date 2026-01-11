-- =======================================================
-- PROJET JAVA - GESTION BIBLIOTHEQUE
-- SCRIPT DE CRÉATION ET D'INSERTION ('biblio')
-- =======================================================

-- 1. NETTOYAGE ET CRÉATION DE LA BASE
DROP DATABASE IF EXISTS biblio;
CREATE DATABASE biblio;
USE biblio;

-- 2. CRÉATION DES TABLES

-- Table Comptes (Admin)
CREATE TABLE comptes (
    id_compte INT PRIMARY KEY,
    login VARCHAR(50) UNIQUE,
    mot_de_passe VARCHAR(100),
    role VARCHAR(20)
);

-- Table Utilisateurs
CREATE TABLE utilisateurs (
    id_utilisateur INT PRIMARY KEY,
    nom VARCHAR(100),
    prenom VARCHAR(100),
    matricule VARCHAR(50) UNIQUE,
    type_utilisateur VARCHAR(20) -- 'ETUDIANT' ou 'ENSEIGNANT'
);

-- Table Livres
CREATE TABLE livres (
    id_livre INT PRIMARY KEY,
    titre VARCHAR(150),
    auteur VARCHAR(100),
    isbn VARCHAR(50) UNIQUE,
    quantite_totale INT,
    quantite_disponible INT
);

-- Table Emprunts
CREATE TABLE emprunts (
    id_emprunt INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT,
    id_livre INT,
    date_emprunt DATE,
    date_retour_prevue DATE,
    date_retour_effective DATE,
    penalite INT DEFAULT 0,
    penalite_payee BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id_utilisateur) ON DELETE CASCADE,
    FOREIGN KEY (id_livre) REFERENCES livres(id_livre) ON DELETE CASCADE
);

-- Table Réservations
CREATE TABLE reservations (
    id_reservation INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT,
    id_livre INT,
    date_reservation DATE,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id_utilisateur) ON DELETE CASCADE,
    FOREIGN KEY (id_livre) REFERENCES livres(id_livre) ON DELETE CASCADE
);

-- =======================================================
-- 3. INSERTION DES DONNÉES 
-- =======================================================

-- Compte Admin
INSERT INTO comptes (id_compte, login, mot_de_passe, role) VALUES 
(1, 'admin', 'admin', 'ADMIN');

-- UTILISATEURS 

-- 20 Étudiants
INSERT INTO utilisateurs (id_utilisateur, nom, prenom, matricule, type_utilisateur) VALUES
(1, 'Diop', 'Moussa', 'ETU202501', 'ETUDIANT'),
(2, 'Ndiaye', 'Fatou', 'ETU202502', 'ETUDIANT'),
(3, 'Sow', 'Amadou', 'ETU202503', 'ETUDIANT'),
(4, 'Fall', 'Awa', 'ETU202504', 'ETUDIANT'),
(5, 'Gueye', 'Cheikh', 'ETU202505', 'ETUDIANT'),
(6, 'Ba', 'Aminata', 'ETU202506', 'ETUDIANT'),
(7, 'Sy', 'Babacar', 'ETU202507', 'ETUDIANT'),
(8, 'Cisse', 'Mariama', 'ETU202508', 'ETUDIANT'),
(9, 'Diallo', 'Oumar', 'ETU202509', 'ETUDIANT'),
(10, 'Faye', 'Khady', 'ETU202510', 'ETUDIANT'),
(11, 'Traore', 'Abdoulaye', 'ETU202511', 'ETUDIANT'),
(12, 'Mendy', 'Sophie', 'ETU202512', 'ETUDIANT'),
(13, 'Gomis', 'Jean', 'ETU202513', 'ETUDIANT'),
(14, 'Wade', 'Astou', 'ETU202514', 'ETUDIANT'),
(15, 'Seck', 'Ibrahima', 'ETU202515', 'ETUDIANT'),
(16, 'Mbaye', 'Ndèye', 'ETU202516', 'ETUDIANT'),
(17, 'Thiam', 'Modou', 'ETU202517', 'ETUDIANT'),
(18, 'Ly', 'Ramata', 'ETU202518', 'ETUDIANT'),
(19, 'Kane', 'Samba', 'ETU202519', 'ETUDIANT'),
(20, 'Sarr', 'Coumba', 'ETU202520', 'ETUDIANT');

-- 10 Enseignants
INSERT INTO utilisateurs (id_utilisateur, nom, prenom, matricule, type_utilisateur) VALUES
(21, 'Diagne', 'Serigne', 'ENS202501', 'ENSEIGNANT'),
(22, 'Niang', 'Moustapha', 'ENS202502', 'ENSEIGNANT'),
(23, 'Camara', 'Aïssatou', 'ENS202503', 'ENSEIGNANT'),
(24, 'Dione', 'Joseph', 'ENS202504', 'ENSEIGNANT'),
(25, 'Tamba', 'Ismaila', 'ENS202505', 'ENSEIGNANT'),
(26, 'Samb', 'Pape', 'ENS202506', 'ENSEIGNANT'),
(27, 'Ka', 'Djibril', 'ENS202507', 'ENSEIGNANT'),
(28, 'Beye', 'Alioune', 'ENS202508', 'ENSEIGNANT'),
(29, 'Dia', 'Mame Diarra', 'ENS202509', 'ENSEIGNANT'),
(30, 'Ndour', 'Youssou', 'ENS202510', 'ENSEIGNANT');

-- LIVRES 
INSERT INTO livres (id_livre, titre, auteur, isbn, quantite_totale, quantite_disponible) VALUES
-- Littérature Africaine / Sénégalaise
(1, 'L\'Aventure ambiguë', 'Cheikh Hamidou Kane', '9782264009', 10, 10),
(2, 'Une si longue lettre', 'Mariama Bâ', '9782842612', 15, 12),
(3, 'Les Bouts de bois de Dieu', 'Ousmane Sembène', '9782266106', 8, 8),
(4, 'Vol de nuit', 'Antoine de Saint-Exupéry', '9782070360', 5, 5),
(5, 'Crépuscule des temps anciens', 'Nazi Boni', '9782708705', 6, 6),
(6, 'L\'Étranger', 'Albert Camus', '978207036002', 12, 12),
(7, 'Le Devoir de violence', 'Yambo Ouologuem', '9782020011', 7, 7),
(8, 'Soundjata ou l\'Épopée mandingue', 'Djibril Tamsir Niane', '9782708700', 9, 9),
(9, 'Maïmouna', 'Abdoulaye Sadji', '9782708701', 10, 10),
(10, 'Le Petit Prince', 'Saint-Exupéry', '9782070612', 20, 18),
(11, 'Apprendre Java en 10 jours', 'Michel Martin', 'ISBN-JAVA-01', 10, 8),
(12, 'Clean Code', 'Robert C. Martin', 'ISBN-CODE-02', 5, 5),
(13, 'Design Patterns (Gang of Four)', 'Erich Gamma', 'ISBN-DP-003', 4, 0), -- INDISPONIBLE (Stock 0 pour test réservation)
(14, 'Programmation Web avec Spring Boot', 'Patrick D', 'ISBN-SPRING-04', 6, 6),
(15, 'Réseaux et Télécoms', 'Claude Servin', 'ISBN-NET-05', 8, 8),
(16, 'Algorithmes et Structures de Données', 'Cormen', 'ISBN-ALGO-06', 10, 10),
(17, 'Le Langage C', 'Kernighan & Ritchie', 'ISBN-C-07', 7, 7),
(18, 'Intelligence Artificielle', 'Stuart Russell', 'ISBN-AI-08', 4, 4),
(19, 'Bases de Données SQL', 'Frédéric Brouard', 'ISBN-SQL-09', 12, 12),
(20, 'Sécurité Informatique', 'Bruce Schneier', 'ISBN-SEC-10', 5, 5);

-- EMPRUNTS (Scénarios de test)

-- A. Emprunts EN COURS (Tout va bien)
-- Moussa Diop (1) a emprunté Java (11)
INSERT INTO emprunts (id_utilisateur, id_livre, date_emprunt, date_retour_prevue) 
VALUES (1, 11, DATE_SUB(CURRENT_DATE, INTERVAL 5 DAY), DATE_ADD(CURRENT_DATE, INTERVAL 9 DAY));

-- Fatou Ndiaye (2) a emprunté Une si longue lettre (2)
INSERT INTO emprunts (id_utilisateur, id_livre, date_emprunt, date_retour_prevue) 
VALUES (2, 2, DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY), DATE_ADD(CURRENT_DATE, INTERVAL 12 DAY));



-- B. Emprunts EN RETARD (Surlignés en ROUGE)
-- Amadou Sow (3) est en retard de 10 jours sur "L'Aventure ambiguë"
INSERT INTO emprunts (id_utilisateur, id_livre, date_emprunt, date_retour_prevue) 
VALUES (3, 1, DATE_SUB(CURRENT_DATE, INTERVAL 24 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 10 DAY));

-- Awa Fall (4) est en retard de 5 jours sur "Clean Code"
INSERT INTO emprunts (id_utilisateur, id_livre, date_emprunt, date_retour_prevue) 
VALUES (4, 12, DATE_SUB(CURRENT_DATE, INTERVAL 19 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 5 DAY));



-- C. Emprunts CLOTURÉS (Historique)
-- Cheikh Gueye (5) a rendu son livre en retard (Pénalité non payée)
INSERT INTO emprunts (id_utilisateur, id_livre, date_emprunt, date_retour_prevue, date_retour_effective, penalite, penalite_payee) 
VALUES (5, 5, DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 15 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 10 DAY), 5, FALSE);

-- Aminata Ba (6) a rendu son livre en retard (Pénalité PAYÉE)
INSERT INTO emprunts (id_utilisateur, id_livre, date_emprunt, date_retour_prevue, date_retour_effective, penalite, penalite_payee) 
VALUES (6, 6, DATE_SUB(CURRENT_DATE, INTERVAL 40 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 35 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY), 5, TRUE);

-- Babacar Sy (7) a rendu à l'heure (Pas de pénalité)
INSERT INTO emprunts (id_utilisateur, id_livre, date_emprunt, date_retour_prevue, date_retour_effective, penalite) 
VALUES (7, 7, DATE_SUB(CURRENT_DATE, INTERVAL 10 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 3 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 3 DAY), 0);

-- Réservation pour tester (Mariama Cisse réserve "Design Patterns" qui a stock 0)
INSERT INTO reservations (id_utilisateur, id_livre, date_reservation) VALUES (8, 13, CURRENT_DATE);



-- 4. RÉGLAGE FINAL AUTO-INCREMENT
-- Pour que les prochains ajouts via l'appli commencent après ID 30 (users) et 20 (livres)
ALTER TABLE utilisateurs MODIFY id_utilisateur INT AUTO_INCREMENT;
ALTER TABLE utilisateurs AUTO_INCREMENT = 31;

ALTER TABLE livres MODIFY id_livre INT AUTO_INCREMENT;
ALTER TABLE livres AUTO_INCREMENT = 21;

ALTER TABLE emprunts MODIFY id_emprunt INT AUTO_INCREMENT;
ALTER TABLE reservations MODIFY id_reservation INT AUTO_INCREMENT;