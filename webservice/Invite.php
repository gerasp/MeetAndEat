<?php
$participants = $_POST['participants'];
$participants = explode(",", $participants);
foreach ($participants as $participant) {
    $sql1 = "SELECT id FROM user WHERE username = '$participant'";
    $getID = mysqli_fetch_assoc($database->executeQuery($sql1));
    $userID = $getID['id'];
    echo $userID;

    $sql2 = "INSERT INTO user_meeting VALUES (
    " . $userID . ",
    " . $meeting . ",
    0,
    0);";
    $result2 = $database->executeQuery($sql2);
}
?>