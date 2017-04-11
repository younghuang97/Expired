<?php
    $db_name = "item";
    $mysql_username = "root";
    $mysql_password = "";
    $server_name = "localhost";
    $mysqli = new mysqli($server_name, $mysql_username, $mysql_password, 
        $db_name);
    if ($mysqli->connect_errno) {
        echo "Connect failed: $mysqli->connect->error\n";
        exit();
    }
    else {
        echo "Connect success!\n";
    }
?>