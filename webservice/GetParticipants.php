<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();
$sql = "SELECT u.username
        FROM user_meeting um, user u
        WHERE meeting_id = ".$_POST['meeting_id']."
        AND isAccepted = 1
        AND u.id = um.user_id;";

$result = $database->executeQuery($sql);

$sql = "SELECT u.username
        FROM user_meeting um, user u
        WHERE meeting_id = ".$_POST['meeting_id']."
        AND isAccepted = 1
        AND isAdmin = 1
        AND u.id = um.user_id;";

$result2 = $database->executeQuery($sql);
if($result) {
    $response["code"] = 2;
    $response["results"] = array();
    while($row = $result->fetch_assoc()) {
        array_push($response["results"],$row);
    }
    $response["admin"] = $result2->fetch_assoc()['username'];

} else {
    $response["code"] = 0;
}
echo json_encode($response);

$database->closeConnection();
?>