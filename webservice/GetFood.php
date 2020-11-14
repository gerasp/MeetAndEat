<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();
$sql = "SELECT f.id, f.icon, f.description, f.amount, u.username
        FROM food f, user u
        WHERE meeting_id = ".$_POST['meeting_id']."
        AND u.id = f.user_id;";

$result = $database->executeQuery($sql);
if($result) {
    $response["code"] = 2;
    $response["results"] = array();
    while($row = $result->fetch_assoc()) {
        array_push($response["results"],$row);
    }
} else {
    $response["code"] = 0;
}
echo json_encode($response);

$database->closeConnection();
?>