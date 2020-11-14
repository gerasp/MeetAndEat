<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();

$sql = "SELECT m.id,title,location,datetime,color,isAdmin
        FROM user_meeting um, meeting m 
        WHERE um.meeting_id = m.id 
        AND DATE(datetime) < DATE(now())
        AND isAccepted = 1
        AND um.user_id = " . $_POST['user_id'] . ";";

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