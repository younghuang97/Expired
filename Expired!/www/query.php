<?php
    require "conn.php";
    $item_name = $_POST["name"];
    $data_one = "exp_date_fri";
    $data_two = "exp_date_fro";
	
    $result = $mysqli->query("SELECT * FROM item_data WHERE name LIKE '$item_name'");
	$fridge = "null";
	$response = array();
	
    if ($result->num_rows > 0)
    {   
        $row = $result->fetch_assoc();
        if ($row["$data_one"])
        {
			$fridge = $row[$data_one];
        }
        if ($row[$data_two])
        {
			$frozen = $row[$data_two];
        }
		array_push($response,array("fridge"=>$fridge, "frozen"=>$frozen));
    }
    else {
		array_push($response,array("fridge"=>$fridge));
    }
	echo json_encode($response);
    $result->close();
    $mysqli->close();
?>