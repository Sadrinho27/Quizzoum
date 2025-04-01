-- Création de la base de données Quizzoum
CREATE DATABASE IF NOT EXISTS Quizzoum;
USE Quizzoum;

-- Création de la table Type_Difficulte
CREATE TABLE IF NOT EXISTS Type_Difficulte (
    idTypeDiff INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(50) NOT NULL
);

-- Création de la table Type_Role
CREATE TABLE IF NOT EXISTS Type_Role (
    idTypeRole INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(50) NOT NULL
);

-- Création de la table Utilisateur
CREATE TABLE IF NOT EXISTS Utilisateur (
    idUser INT PRIMARY KEY AUTO_INCREMENT,
    pseudo VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    idTypeRole INT NOT NULL,
    scoreTotal INT NOT NULL DEFAULT 0,
    FOREIGN KEY (idTypeRole) REFERENCES Type_Role(idTypeRole) ON DELETE CASCADE
);

-- Création de la table Session
CREATE TABLE IF NOT EXISTS Session (
    idSession INT PRIMARY KEY AUTO_INCREMENT,
    date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    score INT NOT NULL DEFAULT 0,
    idUser INT NOT NULL,
    FOREIGN KEY (idUser) REFERENCES Utilisateur(idUser) ON DELETE CASCADE
);

-- Création de la table Theme
CREATE TABLE IF NOT EXISTS Theme (
    idTheme INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

-- Création de la table Question
CREATE TABLE IF NOT EXISTS Question (
    idQuestion INT PRIMARY KEY AUTO_INCREMENT,
    libelle TEXT NOT NULL,
    idTypeDiff INT NOT NULL,
    idTheme INT NOT NULL,
    FOREIGN KEY (idTheme) REFERENCES Theme(idTheme) ON DELETE CASCADE,
    FOREIGN KEY (idTypeDiff) REFERENCES Type_Difficulte(idTypeDiff) ON DELETE CASCADE
);

-- Création de la table Reponse
CREATE TABLE IF NOT EXISTS Reponse (
    idReponse INT PRIMARY KEY AUTO_INCREMENT,
    libelle TEXT NOT NULL,
    isCorrect BOOLEAN NOT NULL DEFAULT FALSE,
    idQuestion INT NOT NULL,
    FOREIGN KEY (idQuestion) REFERENCES Question(idQuestion) ON DELETE CASCADE
);

-- Création de la table Session_Question
CREATE TABLE IF NOT EXISTS Session_Question (
    idSession INT NOT NULL,
    idQuestion INT NOT NULL,
    PRIMARY KEY (idSession, idQuestion),
    FOREIGN KEY (idSession) REFERENCES Session(idSession) ON DELETE CASCADE,
    FOREIGN KEY (idQuestion) REFERENCES Question(idQuestion) ON DELETE CASCADE
);

-- Données pour la table Type_Difficulte
INSERT INTO Type_Difficulte (idTypeDiff, libelle) VALUES (0, "facile"), (1, "moyen"), (2, "difficile");

-- Données pour la table Type_Role
INSERT INTO Type_Role (idTypeRole, libelle) VALUES (0, "Joueur"), (1, "Admin");

-- Données pour la table Theme
INSERT INTO Theme (idTheme, libelle) VALUES (0, 'Informatique'), (1, 'Histoire'), (2, 'Géographie'), (3, 'Science');

-- Question 1
INSERT INTO Question (idQuestion, libelle, idTypeDiff, idTheme) 
VALUES (0, 'Quel est le langage de programmation le plus populaire en 2025 ?', 0, 0);
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (idReponse, libelle, isCorrect, idQuestion) 
VALUES (0, 'Python', TRUE, @idQuestion), (1, 'Java', FALSE, @idQuestion),
       (2, 'C++', FALSE, @idQuestion), (3, 'Ruby', FALSE, @idQuestion);

ALTER TABLE Question AUTO_INCREMENT = 1;
ALTER TABLE Reponse AUTO_INCREMENT = 4;

-- Question 2
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Quel est le principal système d\'exploitation utilisé sur les serveurs web ?", 1, 0);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Linux', TRUE, @idQuestion), ('Windows Server', FALSE, @idQuestion), 
       ('macOS', FALSE, @idQuestion), ('Android', FALSE, @idQuestion);

-- Question 3
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quel est le nom du protocole utilisé pour sécuriser les connexions sur le web ?', 2, 0);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('HTTPS', TRUE, @idQuestion), ('FTP', FALSE, @idQuestion), 
       ('HTTP', FALSE, @idQuestion), ('SSH', FALSE, @idQuestion);

-- Question 4
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quel est le principal langage utilisé pour développer des applications Android ?', 0, 0);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Kotlin', TRUE, @idQuestion), ('Java', FALSE, @idQuestion), 
       ('Python', FALSE, @idQuestion), ('C#', FALSE, @idQuestion);

-- Question 5
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Quel est l\'algorithme de compression utilisé par le format ZIP ?", 1, 0);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Deflate', TRUE, @idQuestion), ('Huffman', FALSE, @idQuestion), 
       ('LZ77', FALSE, @idQuestion), ('AES', FALSE, @idQuestion);

-- Question 6
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Qu\'est-ce que l\'intelligence artificielle ?', 2, 0);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ("L\'intelligence artificielle est un ensemble de technologies permettant de simuler des comportements intelligents.", TRUE, @idQuestion),
       ("L\'intelligence artificielle est une forme d\'énergie renouvelable.", FALSE, @idQuestion),
       ("L\'intelligence artificielle est un système permettant de récolter des informations personnelles.", FALSE, @idQuestion),
       ("L\'intelligence artificielle est un logiciel de gestion de réseaux sociaux.", FALSE, @idQuestion);

-- Question 7
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Qui a découvert l\'Amérique en 1492 ?", 0, 1);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Christophe Colomb', TRUE, @idQuestion), ('Marco Polo', FALSE, @idQuestion), 
       ('Ferdinand Magellan', FALSE, @idQuestion), ('Vasco de Gama', FALSE, @idQuestion);

-- Question 8
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quel traité a mis fin à la Première Guerre mondiale ?', 1, 1);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Le traité de Versailles', TRUE, @idQuestion), ('Le traité de Paris', FALSE, @idQuestion), 
       ('Le traité de Rome', FALSE, @idQuestion), ('Le traité de Genève', FALSE, @idQuestion);

-- Question 9
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Qui était le dernier tsar de Russie ?', 2, 1);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Nicolas II', TRUE, @idQuestion), ('Alexandre II', FALSE, @idQuestion), 
       ('Pierre le Grand', FALSE, @idQuestion), ('Nicolas Ier', FALSE, @idQuestion);

-- Question 10
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quel est le nom du premier empereur de Chine ?', 0, 1);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Qin Shi Huang', TRUE, @idQuestion), ('Liu Bang', FALSE, @idQuestion), 
       ('Han Wudi', FALSE, @idQuestion), ('Tang Taizong', FALSE, @idQuestion);

-- Question 11
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('En quelle année l\'Empire Romain d\'Occident est-il tombé ?', 1, 1);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('476', TRUE, @idQuestion), ('1453', FALSE, @idQuestion), 
       ('1492', FALSE, @idQuestion), ('1215', FALSE, @idQuestion);

-- Question 12
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quelle est la capitale de la France ?', 0, 2);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Paris', TRUE, @idQuestion), ('Lyon', FALSE, @idQuestion), 
       ('Marseille', FALSE, @idQuestion), ('Toulouse', FALSE, @idQuestion);

-- Question 13
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quel est le plus long fleuve du monde ?', 1, 2);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Le Nil', TRUE, @idQuestion), ("L\'Amazone", FALSE, @idQuestion), 
       ('Le Mississippi', FALSE, @idQuestion), ('Le Yangzi', FALSE, @idQuestion);

-- Question 14
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Quel pays possède le plus grand nombre d\'îles au monde ?", 2, 2);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Suède', TRUE, @idQuestion), ('Finlande', FALSE, @idQuestion), 
       ('Indonésie', FALSE, @idQuestion), ('Canada', FALSE, @idQuestion);

-- Question 15
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quel est le pays le plus vaste du monde ?', 0, 2);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Russie', TRUE, @idQuestion), ('Canada', FALSE, @idQuestion), 
       ('Chine', FALSE, @idQuestion), ('États-Unis', FALSE, @idQuestion);

-- Question 16
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Quel est l\'océan le plus vaste ?", 1, 2);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Pacifique', TRUE, @idQuestion), ('Atlantique', FALSE, @idQuestion), 
       ('Indien', FALSE, @idQuestion), ('Arctique', FALSE, @idQuestion);

-- Question 17
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Quelle est la formule chimique de l\'eau ?", 0, 3);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('H2O', TRUE, @idQuestion), ('CO2', FALSE, @idQuestion), 
       ('O2', FALSE, @idQuestion), ('H2SO4', FALSE, @idQuestion);

-- Question 18
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Quel est l\'élément chimique représenté par le symbole Na ?", 1, 3);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Le Sodium', TRUE, @idQuestion), ('Le Néon', FALSE, @idQuestion), 
       ('Le Nickel', FALSE, @idQuestion), ('Le Nitrate', FALSE, @idQuestion);

-- Question 19
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ( "Quelle est la principale source d\'énergie du Soleil ?", 2, 3);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('La fusion nucléaire', TRUE, @idQuestion), ('La fission nucléaire', FALSE, @idQuestion), 
       ('La combustion chimique', FALSE, @idQuestion), ("L\'absorption des rayons cosmiques", FALSE, @idQuestion);

-- Question 20
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Le corps humain possède combien d\'os ?", 0, 3);
SET @idQuestion = LAST_INSERT_ID(); -- Récupérer l'id auto-incrémenté
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('206', TRUE, @idQuestion), ('205', FALSE, @idQuestion), 
       ('210', FALSE, @idQuestion), ('202', FALSE, @idQuestion);