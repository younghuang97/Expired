<?php
    require "conn.php";
    $item_name = "Cabbage";
    $data_one = "exp_date_fri";
    $data_two = "exp_date_fro";
    $result = $mysqli->query("SELECT * FROM item_data WHERE name LIKE '$item_name'");
    if ($result->num_rows > 0)
    {   
        $row = $result->fetch_assoc();
        if ($row["$data_one"])
        {
            echo "$item_name can be refridgerated for $row[$data_one] days.\n";
        }
        if ($row[$data_two])
        {
            echo "$item_name can be frozen for $row[$data_two] days.\n";
        }
    }
    else {
        echo "No expiration dates stored in database";
    }
    $result->close();
    $mysqli->close();
?>