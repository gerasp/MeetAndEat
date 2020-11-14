<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();

$sql = "SELECT * FROM user WHERE username='" . $_POST['username'] . "'";
$result = $database->executeQuery($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    if (password_verify($_POST['password'], $row['password'])) {
        $response["code"] = 2;
        $response["id"] = $row['id'];
        $response["username"] = $row['username'];
    } else {
        $response["code"] = 1;
    }
} else {
    $response["code"] = 0;
}
echo json_encode($response);

$database->closeConnection();
?>