<?php
header("Content-Type: application/json");

$host = "localhost";
$username = "quizzoum";
$password = "RRle@]6/7F3mF1mM";
$dbName = "quizzoum";

// Connexion à la base de données
$conn = new mysqli($host, $username, $password, $dbName);

if ($conn->connect_error) {
    die(json_encode(["error" => "Connection failed: " . $conn->connect_error]));
}

$method = $_SERVER['REQUEST_METHOD'];
$resource = isset($_GET['resource']) ? $_GET['resource'] : null;

if ($method === 'GET') {
    if ($resource == 'users') {
        $result = $conn->query("SELECT idUser, pseudo, password, idTypeRole AS isAdmin, scoreTotal FROM Utilisateur");

        if ($result) {
            $users = $result->fetch_all(MYSQLI_ASSOC);
            echo json_encode($users);
        } else {
            echo json_encode(["error" => "Unable to fetch users"]);
        }
    } elseif ($resource == 'questions') {
        $result = $conn->query("SELECT idQuestion, Q.libelle, D.libelle AS difficulty, T.libelle AS theme FROM Question Q JOIN type_difficulte D ON Q.idTypeDiff = D.idTypeDiff JOIN theme T ON Q.idTheme = T.idTheme");
        // $result = $conn->query("SELECT Q.libelle, D.libelle AS difficulte, T.libelle AS theme FROM Question Q JOIN type_difficulte D ON Q.idTypeDiff = D.idTypeDiff JOIN theme T ON Q.idTheme = T.idTheme");

        if ($result) {
            $questions = $result->fetch_all(MYSQLI_ASSOC);

            foreach ($questions as &$question) {
                $idQuestion = $question['idQuestion'];
                $reponsesResult = $conn->query("SELECT idReponse, libelle, isCorrect FROM Reponse WHERE idQuestion = $idQuestion");

                if ($reponsesResult) {
                    $reponses = [];
                    while ($reponse = $reponsesResult->fetch_assoc()) {
                        $reponses[] = [
                            'idReponse' => $reponse['idReponse'],
                            'libelle' => $reponse['libelle'],
                            'isCorrect' => (bool)$reponse['isCorrect']
                        ];
                    }
                    $question['reponses'] = $reponses;
                }
            }

            echo json_encode($questions);
        } else {
            echo json_encode(["error" => "Unable to fetch questions"]);
        }
    } elseif ($resource == 'leaderboard') {
        $result = $conn->query("SELECT pseudo, scoreTotal AS score FROM Utilisateur ORDER BY score DESC");

        if ($result) {
            $users = $result->fetch_all(MYSQLI_ASSOC);
            echo json_encode($users);
        } else {
            echo json_encode(["error" => "Unable to fetch users"]);
        }
    } else {
        echo json_encode(["error" => "Resource not found"]);
    }
} elseif ($method === 'POST') {
    $data = json_decode(file_get_contents("php://input"), true);

    if ($resource == 'users') {
        if (!isset($data['pseudo']) || !isset($data['password']) || !isset($data['isAdmin'])) {
            echo json_encode(["error" => "Missing required fields"]);
            exit();
        }
    
        $pseudo = $conn->real_escape_string($data['pseudo']);
        $password = $data['password'];
        $isAdmin = (int)$data['isAdmin'];
    
        $stmt = $conn->prepare("INSERT INTO Utilisateur (pseudo, password, idTypeRole) VALUES (?, ?, ?)");
        $stmt->bind_param("ssi", $pseudo, $password, $isAdmin);

        if ($stmt->execute()) {
            echo json_encode(["success" => "User created successfully"]);
        } else {
            echo json_encode(["error" => "Failed to create user"]);
        }
    
        $stmt->close();
    } elseif ($resource == 'users_by_pseudo') {
        if (!isset($data['pseudo'])) {
            echo json_encode(["error" => "Pseudo requis"]);
            exit();
        }
    
        $pseudo = $conn->real_escape_string($data['pseudo']);
        
        $stmt = $conn->prepare("SELECT idUser, pseudo, password, idTypeRole AS isAdmin, scoreTotal FROM Utilisateur WHERE pseudo = ?");
        $stmt->bind_param("s", $pseudo);
        $stmt->execute();
        $result = $stmt->get_result();
    
        if ($result->num_rows > 0) {
            $user = $result->fetch_assoc();
            echo json_encode($user);
        } else {
            echo json_encode(["error" => "Utilisateur non trouvé"]);
        }
    } elseif ($resource == 'update_user_role') {
        if (!isset($data['pseudo']) || !isset($data['newRole'])) {
            echo json_encode(["error" => "Pseudo et nouveau rôle requis"]);
            exit();
        }
    
        $pseudo = $conn->real_escape_string($data['pseudo']);
        $newRole = (int) $data['newRole'];
    
        $stmt = $conn->prepare("UPDATE Utilisateur SET idTypeRole = ? WHERE pseudo = ?");
        $stmt->bind_param("is", $newRole, $pseudo);
    
        if ($stmt->execute()) {
            echo json_encode(["success" => "Rôle mis à jour"]);
        } else {
            echo json_encode(["error" => "Échec de la mise à jour"]);
        }
    } elseif ($resource == 'update_user_score') {
        if (!isset($data['pseudo']) || !isset($data['score'])) {
            echo json_encode(["error" => "Pseudo et score de session requis"]);
            exit();
        }
    
        $pseudo = $conn->real_escape_string($data['pseudo']);
        $sessionScore = (int) $data['score'];
    
        // Récupérer le score total actuel
        $stmt = $conn->prepare("SELECT scoreTotal FROM Utilisateur WHERE pseudo = ?");
        $stmt->bind_param("s", $pseudo);
        $stmt->execute();
        $result = $stmt->get_result();
        
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $currentTotalScore = $row['scoreTotal'];
            
            // Additionner le score de la session au score total
            $newTotalScore = $currentTotalScore + $sessionScore;
            
            // Mettre à jour le score total dans la base de données
            $stmt = $conn->prepare("UPDATE Utilisateur SET scoreTotal = ? WHERE pseudo = ?");
            $stmt->bind_param("is", $newTotalScore, $pseudo);
            
            if ($stmt->execute()) {
                echo json_encode(["success" => "Score mis à jour"]);
            } else {
                echo json_encode(["error" => "Échec de la mise à jour du score"]);
            }
        } else {
            echo json_encode(["error" => "Utilisateur introuvable"]);
        }
    } elseif ($resource == 'delete_user') {
        if (!isset($data['pseudo'])) {
            echo json_encode(["error" => "Pseudo requis"]);
            exit();
        }
    
        $pseudo = $conn->real_escape_string($data['pseudo']);
    
        // Vérifier si l'utilisateur existe avant suppression
        $stmt = $conn->prepare("SELECT pseudo FROM Utilisateur WHERE pseudo = ?");
        $stmt->bind_param("s", $pseudo);
        $stmt->execute();
        $result = $stmt->get_result();
    
        if ($result->num_rows == 0) {
            echo json_encode(["error" => "Utilisateur non trouvé"]);
            exit();
        }
    
        // Supprimer l'utilisateur
        $stmt = $conn->prepare("DELETE FROM Utilisateur WHERE pseudo = ?");
        $stmt->bind_param("s", $pseudo);
        
        if ($stmt->execute()) {
            echo json_encode(["success" => "Utilisateur supprimé"]);
        } else {
            echo json_encode(["error" => "Erreur lors de la suppression"]);
        }
    } elseif ($resource == 'sessions') {
        if (!isset($data['score']) || !isset($data['idUser'])) {
            http_response_code(400);
            echo json_encode(["error" => "Score et identifiant utilisateur requis"]);
            exit();
        }
    
        $score = $conn->real_escape_string($data['score']);
        $idUser = intval($data['idUser']);
    
        $stmt = $conn->prepare("INSERT INTO Session (score, idUser) VALUES (?, ?)");
        $stmt->bind_param("ii", $score, $idUser);
    
        if ($stmt->execute()) {
            $sessionId = $stmt->insert_id;  // Récupération de l'ID de la session
            echo json_encode($sessionId);
        } else {
            http_response_code(500);
            echo json_encode(["error" => "Échec de l'enregistrement de la session"]);
        }
    
        $stmt->close();
    } elseif ($resource == 'questions') {
        if (!isset($data['libelle']) || !isset($data['idTheme']) || !isset($data['reponses']) || !is_array($data['reponses'])) {
            echo json_encode(["error" => "Missing required fields"]);
            exit();
        }

        $libelle = $conn->real_escape_string($data['libelle']);
        $idTheme = (int)$data['idTheme'];

        $conn->begin_transaction();

        try {
            $stmt = $conn->prepare("INSERT INTO Question (libelle, idTheme) VALUES (?, ?)");
            $stmt->bind_param("si", $libelle, $idTheme);
            if (!$stmt->execute()) {
                throw new Exception("Failed to insert question");
            }
            $idQuestion = $conn->insert_id;
            $stmt->close();

            $stmt = $conn->prepare("INSERT INTO Reponse (idQuestion, libelle, isCorrect) VALUES (?, ?, ?)");
            foreach ($data['reponses'] as $reponse) {
                if (!isset($reponse['libelle']) || !isset($reponse['isCorrect'])) {
                    throw new Exception("Invalid response format");
                }
                $libelleReponse = $conn->real_escape_string($reponse['libelle']);
                $isCorrect = (int)$reponse['isCorrect'];
                $stmt->bind_param("isi", $idQuestion, $libelleReponse, $isCorrect);
                if (!$stmt->execute()) {
                    throw new Exception("Failed to insert response");
                }
            }
            $stmt->close();

            $conn->commit();
            echo json_encode(["success" => "Question and answers added successfully"]);

        } catch (Exception $e) {
            $conn->rollback();
            echo json_encode(["error" => $e->getMessage()]);
        }
    } elseif ($resource == 'session_questions') {
        if (!isset($data['idSession']) || !isset($data['questions']) || !is_array($data['questions'])) {
            http_response_code(400);
            echo json_encode(["error" => "ID de session et liste des questions requis"]);
            exit();
        }
    
        $idSession = intval($data['idSession']);
        $questions = $data['questions'];
    
        $stmt = $conn->prepare("INSERT INTO Session_Question (idSession, idQuestion) VALUES (?, ?)");
    
        foreach ($questions as $idQuestion) {
            $idQuestion = intval($idQuestion);
            $stmt->bind_param("ii", $idSession, $idQuestion);
            if (!$stmt->execute()) {
                http_response_code(500);
                echo json_encode(["error" => "Échec de l'ajout d'une question à la session"]);
                exit();
            }
        }
    
        echo json_encode(["success" => "Questions enregistrées avec succès"]);
        $stmt->close();
    } else {
        echo json_encode(["error" => "Invalid resource for POST method"]);
    }
} else {
    echo json_encode(["error" => "Invalid request method"]);
}

$conn->close();
?>
