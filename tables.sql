CREATE TABLE Utilisateur (
    idUser INT PRIMARY KEY AUTO_INCREMENT,
    pseudo VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Stockage sécurisé du mot de passe (hashé)
    scoreTotal INT NOT NULL DEFAULT 0
);

CREATE TABLE Session (
    idSession INT PRIMARY KEY AUTO_INCREMENT,
    date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    score INT NOT NULL DEFAULT 0,
    duree INT NOT NULL, -- Durée en secondes
    idUser INT NOT NULL,
    FOREIGN KEY (idUser) REFERENCES Utilisateur(idUser) ON DELETE CASCADE
);

CREATE TABLE Theme (
    idTheme INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Question (
    idQuestion INT PRIMARY KEY AUTO_INCREMENT,
    libelle TEXT NOT NULL,
    difficulte ENUM('facile', 'moyen', 'difficile') NOT NULL,
    idTheme INT NOT NULL,
    FOREIGN KEY (idTheme) REFERENCES Theme(idTheme) ON DELETE CASCADE
);

CREATE TABLE Reponse (
    idReponse INT PRIMARY KEY AUTO_INCREMENT,
    libelle TEXT NOT NULL,
    isCorrect BOOLEAN NOT NULL DEFAULT FALSE,
    idQuestion INT NOT NULL,
    FOREIGN KEY (idQuestion) REFERENCES Question(idQuestion) ON DELETE CASCADE
);

CREATE TABLE Session_Question (
    idSession INT NOT NULL,
    idQuestion INT NOT NULL,
    PRIMARY KEY (idSession, idQuestion),
    FOREIGN KEY (idSession) REFERENCES Session(idSession) ON DELETE CASCADE,
    FOREIGN KEY (idQuestion) REFERENCES Question(idQuestion) ON DELETE CASCADE
);
