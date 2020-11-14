<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();

$participants = $_POST['participants'];
if ($participants) {
    $participants = explode(",", $participants);
    if ($participants) {
        foreach ($participants as $participant) {
            $sql = "SELECT id FROM user WHERE username = '$participant';";
            $result = $database->executeQuery($sql)->fetch_assoc();
            $userID = $result['id'];
            $sql = "INSERT INTO user_meeting VALUES (
                    " . $userID . "," . $_POST['meeting_id'] . ",0,0);";
            $result = $database->executeQuery($sql);
            if (!$result) {
                $response["code"] = 0;
                break;
            }
        }
    }
}

if ($result) {
    $response["code"] = 2;
    echo json_encode($response);
} else {
    $response["code"] = 0;
    echo json_encode($response);
}

$sql = "DELETE FROM meeting
        WHERE datetime < DATE_SUB(NOW() , INTERVAL 10 DAY)";
$database->executeQuery($sql);
$database->closeConnection();
?>