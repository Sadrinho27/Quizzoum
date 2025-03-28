-- Création de la base de données Quizzoum
CREATE DATABASE IF NOT EXISTS Quizzoum;
USE Quizzoum;

-- Création de la table type_difficulte
CREATE TABLE IF NOT EXISTS type_difficulte (
    idTypeDiff INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(50) NOT NULL
);

-- Création de la table Utilisateur
CREATE TABLE IF NOT EXISTS Utilisateur (
    idUser INT PRIMARY KEY AUTO_INCREMENT,
    pseudo VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    scoreTotal INT NOT NULL DEFAULT 0
);

-- Création de la table Session
CREATE TABLE IF NOT EXISTS Session (
    idSession INT PRIMARY KEY AUTO_INCREMENT,
    date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    score INT NOT NULL DEFAULT 0,
    duree INT NOT NULL,
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
    FOREIGN KEY (idTypeDiff) REFERENCES type_difficulte(idTypeDiff) ON DELETE CASCADE
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


-- Données pour la table type_difficulte
INSERT INTO type_difficulte (libelle) VALUES ("facile");
INSERT INTO type_difficulte (libelle) VALUES ("moyen");
INSERT INTO type_difficulte (libelle) VALUES ("difficile");


-- Données pour la table Theme
INSERT INTO Theme (libelle) VALUES ('Informatique');
INSERT INTO Theme (libelle) VALUES ('Histoire');
INSERT INTO Theme (libelle) VALUES ('Géographie');
INSERT INTO Theme (libelle) VALUES ('Science');


-- Données pour la table Question de Theme Informatique
-- Facile
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quel est le langage de programmation le plus populaire en 2025 ?', 1, 1); -- Type de difficulté 'facile' et thème 'Informatique'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Python', TRUE, @idQuestion), 
       ('Java', FALSE, @idQuestion), 
       ('C++', FALSE, @idQuestion), 
       ('Ruby', FALSE, @idQuestion);

-- Moyen
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Quel est le principal système d'exploitation utilisé sur les serveurs web ?", 2, 1); -- Difficulté 'moyen' et thème 'Informatique'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Linux', TRUE, @idQuestion), 
       ('Windows Server', FALSE, @idQuestion), 
       ('macOS', FALSE, @idQuestion), 
       ('Android', FALSE, @idQuestion);

-- Difficile
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quel est le nom du protocole utilisé pour sécuriser les connexions sur le web ?', 3, 1); -- Difficulté 'difficile' et thème 'Informatique'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('HTTPS', TRUE, @idQuestion), 
       ('FTP', FALSE, @idQuestion), 
       ('HTTP', FALSE, @idQuestion), 
       ('SSH', FALSE, @idQuestion);


-- Données pour la table Question de Theme Histoire
-- Facile
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Qui a découvert l'Amérique en 1492 ?", 1, 2); -- Type de difficulté 'facile' et thème 'Histoire'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Christophe Colomb', TRUE, @idQuestion), 
       ('Marco Polo', FALSE, @idQuestion), 
       ('Ferdinand Magellan', FALSE, @idQuestion), 
       ('Vasco de Gama', FALSE, @idQuestion);

-- Moyen
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quel traité a mis fin à la Première Guerre mondiale ?', 2, 2); -- Difficulté 'moyen' et thème 'Histoire'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Le traité de Versailles', TRUE, @idQuestion), 
       ('Le traité de Paris', FALSE, @idQuestion), 
       ('Le traité de Rome', FALSE, @idQuestion), 
       ('Le traité de Genève', FALSE, @idQuestion);

-- Difficile
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Qui était le dernier tsar de Russie ?', 3, 2); -- Difficulté 'difficile' et thème 'Histoire'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Nicolas II', TRUE, @idQuestion), 
       ('Alexandre II', FALSE, @idQuestion), 
       ('Pierre le Grand', FALSE, @idQuestion), 
       ('Nicolas Ier', FALSE, @idQuestion);


-- Données pour la table Question de Theme Géographie
-- Facile
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quelle est la capitale de la France ?', 1, 3); -- Type de difficulté 'facile' et thème 'Géographie'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Paris', TRUE, @idQuestion), 
       ('Lyon', FALSE, @idQuestion), 
       ('Marseille', FALSE, @idQuestion), 
       ('Toulouse', FALSE, @idQuestion);

-- Moyen
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ('Quel est le plus long fleuve du monde ?', 2, 3); -- Difficulté 'moyen' et thème 'Géographie'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Le Nil', TRUE, @idQuestion), 
       ("L'Amazone", FALSE, @idQuestion), 
       ('Le Mississippi', FALSE, @idQuestion), 
       ('Le Yangzi', FALSE, @idQuestion);

-- Difficile
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Quel pays possède le plus grand nombre d'îles au monde ?", 3, 3); -- Difficulté 'difficile' et thème 'Géographie'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Suède', TRUE, @idQuestion), 
       ('Finlande', FALSE, @idQuestion), 
       ('Indonésie', FALSE, @idQuestion), 
       ('Canada', FALSE, @idQuestion);


-- Données pour la table Question de Theme Science
-- Facile
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Quelle est la formule chimique de l'eau ?", 1, 4); -- Type de difficulté 'facile' et thème 'Science'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('H2O', TRUE, @idQuestion), 
       ('CO2', FALSE, @idQuestion), 
       ('O2', FALSE, @idQuestion), 
       ('H2SO4', FALSE, @idQuestion);

-- Moyen
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Quel est l'élément chimique représenté par le symbole 'Na' ?", 2, 4); -- Difficulté 'moyen' et thème 'Science'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('Le Sodium', TRUE, @idQuestion), 
       ('Le Néon', FALSE, @idQuestion), 
       ('Le Nickel', FALSE, @idQuestion), 
       ('Le Nitrate', FALSE, @idQuestion);

-- Difficile
INSERT INTO Question (libelle, idTypeDiff, idTheme) 
VALUES ("Quelle est la principale source d'énergie du Soleil ?", 3, 4); -- Difficulté 'difficile' et thème 'Science'
SET @idQuestion = LAST_INSERT_ID();
INSERT INTO Reponse (libelle, isCorrect, idQuestion) 
VALUES ('La fusion nucléaire', TRUE, @idQuestion), 
       ('La fission nucléaire', FALSE, @idQuestion), 
       ('La combustion chimique', FALSE, @idQuestion), 
       ("L'absorption des rayons cosmiques", FALSE, @idQuestion);
