<?php
require_once("Database.php");
$database = new Database($_POST['key']);
$database->openConnection();

$response = array();
switch ($_POST['type']) {
    case 0:
        $sql = "UPDATE meeting
                SET title = '" . $_POST['parameter'] . "'
                WHERE id = " . $_POST['meeting_id'] . ";";break;
    case 1:
        $sql = "UPDATE meeting
                SET location = '" . $_POST['parameter'] . "'
                WHERE id = " . $_POST['meeting_id'] . ";";break;
    case 2:
        $sql = "UPDATE meeting
                SET datetime = '" . $_POST['parameter'] . "'
                WHERE id = " . $_POST['meeting_id'] . ";";break;
    case 3:
        $sql = "UPDATE meeting
                SET color = '" . $_POST['parameter'] . "'
                WHERE id = " . $_POST['meeting_id'] . ";";break;
    case 4:
        $sql = "DELETE FROM user_meeting
                WHERE meeting_id = " . $_POST['meeting_id'] . "
                AND user_id IN
                    (SELECT id FROM user WHERE username = '" . $_POST['parameter'] . "');";break;
    case 5:
        $sql = "DELETE FROM meeting
                WHERE id = " . $_POST['meeting_id'] . ";";break;

    case 6:
        $sql = "UPDATE user_meeting
                SET isAdmin = 0
                WHERE user_id = ".$_POST['user_id']."
                AND meeting_id = ".$_POST['meeting_id'].";
                
                UPDATE user_meeting
                SET isAdmin = 1
                WHERE user_id IN (SELECT id FROM user WHERE id = ".$_POST['user_id'].")
                AND meeting_id = ".$_POST['meeting_id'].";
                ";break;
    case 7:
        $sql = "DELETE FROM user_meeting
                WHERE user_id = ".$_POST['user_id']."
                AND meeting_id = ".$_POST['meeting_id'].";";break;

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