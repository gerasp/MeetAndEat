<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();

$sql = "INSERT INTO meeting VALUES (NULL,
'" . $_POST['title'] . "',
'" . $_POST['location'] . "',
'" . $_POST['datetime'] . "',
'" . $_POST['color'] . "');";
$result1 = $database->executeQuery($sql);

$meeting = $database->getLastId();
$sql = "INSERT INTO user_meeting VALUES (
" . $_POST['user_id'] . ",
" . $meeting . ",
1,
1);";
$result2 = $database->executeQuery($sql);

$participants = $_POST['participants'];
if ($participants) {
    $participants = explode(",", $participants);
    if ($participants)
        foreach ($participants as $participant) {
            $sql = "SELECT id FROM user WHERE username = '$participant';";
            $result3 = $database->executeQuery($sql);
            $result3 = $result3->fetch_assoc();
            $userID = $result3['id'];
            $sql = "INSERT INTO user_meeting VALUES (
    " . $userID . ",
    " . $meeting . ",
    0,
    0);";
            $result2 = $database->executeQuery($sql);
            if (!$result2) {
                $response["code"] = 0;
                break;
            }
        }
}

if ($result1 && $result2) {
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