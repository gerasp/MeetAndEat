<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();

$sql = "SELECT * 
        FROM message 
        WHERE meeting_id = ".$_POST['meeting_id']."
        ORDER BY timestamp desc;";

$result = $database->executeQuery($sql);
if ($result) {
    $response["code"] = 2;
    $response["results"] = array();
    while ($row = $result->fetch_assoc()) {
        array_push($response["results"], $row);
    }
} else {
    $response["code"] = 0;
}
echo json_encode($response);

$database->closeConnection();
?>