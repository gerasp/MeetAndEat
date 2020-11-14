<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();
if ($_POST['type'] == 0) {
    if ($_POST['accept'] == 1) {
        $sql = "UPDATE user_meeting
    SET isAccepted = 1
    WHERE user_id = " . $_POST['user_id'] . "
    AND meeting_id = " . $_POST['meeting_id'] . ";";
    } else {
        $sql = "DELETE FROM user_meeting
    WHERE user_id = " . $_POST['user_id'] . "
    AND meeting_id = " . $_POST['meeting_id'] . ";";
    }
} else if ($_POST['type'] == 1) {
    if ($_POST['accept'] == 1) {
        $sql = "UPDATE user_user
    SET isAccepted = 1
    WHERE user2_id = " . $_POST['user_id'] . "
    AND user1_id = " . $_POST['meeting_id'] . ";";
    } else {
        $sql = "DELETE FROM user_user
    WHERE user2_id = " . $_POST['user_id'] . "
    AND user1_id = " . $_POST['meeting_id'] . ";";
    }
}
$result = $database->executeQuery($sql);
if ($result) {
    $response["code"] = 2;
} else {
    $response["code"] = 0;
}

echo json_encode($response);

$database->closeConnection();
?>