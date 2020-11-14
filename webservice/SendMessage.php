<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();

$sql = "INSERT INTO message VALUES (null,
'".$_POST['content']."',
".$_POST['timestamp'].",
".$_POST['meeting_id'].",
'".$_POST['user']."');";
$result = $database->executeQuery($sql);

if ($result) {
    $response["code"] = 2;
    echo json_encode($response);
} else {
    $response["code"] = 0;
    echo json_encode($response);
}

$database->closeConnection();
?>