<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();

$sql = "INSERT INTO food VALUES (null,
'".$_POST['icon']."',
'".$_POST['description']."',
'".$_POST['amount']."',
'".$_POST['meeting_id']."',
'".$_POST['user_id']."');";
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