<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();

$sql = "SELECT id, title
        FROM meeting
        WHERE id in (
        SELECT meeting_id
        FROM user_meeting 
        WHERE user_id = ".$_POST['user_id']."
        AND isAccepted = 0);";

$result = $database->executeQuery($sql);

$sql = "SELECT id, username
        FROM user
        WHERE id in (
        SELECT user1_id
        FROM user_user 
        WHERE user2_id = ".$_POST['user_id']."
        AND isAccepted = 0);";

$result2 = $database->executeQuery($sql);

if ($result && $result2) {
    $response["code"] = 2;
    $response["results"] = array();
    while ($row = $result->fetch_assoc()) {
        array_push($response["results"], $row);
    }
    $response["results2"] = array();
    while ($row = $result2->fetch_assoc()) {
        array_push($response["results2"], $row);
    }
} else {
    $response["code"] = 0;
}
echo json_encode($response);

$database->closeConnection();
?>