<?php

	$username="admin";
	$userpass="infs3202";
	$dbhost="localhost";
	$dbdatabase="infs3202";
	$matches = array();

	$db_connect = mysql_connect($dbhost,$username,$userpass) or die("Unable to connect to MySQL");

	$selected = mysql_select_db($dbdatabase,$db_connect) or die("Could not select infs3202");

	if(isset($_POST['func']) && isset($_POST['id']) && $_POST['func']=="getdata"){
		$result = mysql_query("SELECT * FROM itemdata WHERE id = '{$_POST['id']}'");
		if($result){
			while($row = mysql_fetch_array($result)){
				echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review'];
			}
		}
	}

	if(isset($_POST['func']) && isset($_POST['id']) && isset($_POST['category']) 
		&& isset($_POST['name']) && isset($_POST['price']) && isset($_POST['dueTime']) 
		&& isset($_POST['location']) && isset($_POST['description']) && isset($_POST['imageLink']) 
		&& isset($_POST['review']) && $_POST['func']=="updatedata"){

		mysql_query("UPDATE itemdata SET category = '{$_POST['category']}', name = '{$_POST['name']}',price = '{$_POST['price']}',
			dueTime = '{$_POST['dueTime']}', location = '{$_POST['location']}', description = '{$_POST['description']}', imageLink = '{$_POST['imageLink']}',
			review = '{$_POST['review']}' WHERE id = '{$_POST['id']}'");
	}

	if(isset($_POST['func']) && isset($_POST['category']) 
		&& isset($_POST['name']) && isset($_POST['price']) && isset($_POST['dueTime']) 
		&& isset($_POST['location']) && isset($_POST['description']) && isset($_POST['imageLink']) 
		&& isset($_POST['review']) && $_POST['func']=="adddata"){

		mysql_query("INSERT INTO itemdata (id, category, name, price, dueTime, location, 
			description, imageLink, review) VALUES (NULL,'{$_POST['category']}','{$_POST['name']}',
			'{$_POST['price']}','{$_POST['dueTime']}','{$_POST['location']}','{$_POST['description']}', 
			'{$_POST['imageLink']}','{$_POST['review']}') ");
	}

	if(isset($_POST['func']) && isset($_POST['id']) && $_POST['func']=="deletedata"){
		mysql_query("DELETE FROM itemdata WHERE id = '{$_POST['id']}'");
	}

	if(isset($_POST['input'])){

		if(count(explode(" ",$_POST['input']))>1) {
			$input1 = explode(" ",$_POST['input'])[0];
			$input2 = explode(" ",$_POST['input'])[1];
			if (strpos($input2, '>') !== false) {
				$p = str_replace(">","",$input2);
				$result = mysql_query("SELECT * FROM itemdata WHERE price > $p AND name LIKE '%{$input1}%' OR location LIKE '%{$input1}%'");
				if($result) {
					while($row = mysql_fetch_array($result)){
						echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review']."@";
					}				
				}

			} 
			if (strpos($input2, '<') !== false) {
				$p = str_replace("<","",$input2);
				$result = mysql_query("SELECT * FROM itemdata WHERE price < $p AND name LIKE '%{$input1}%' OR location LIKE '%{$input1}%'");
				if($result) {
					while($row = mysql_fetch_array($result)){
						echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review']."@";
					}				
				}
			} 

		}

		else {
			if (strpos($_POST['input'], '>') !== false) {
				$p = str_replace(">","",$_POST['input']);
				$result = mysql_query("SELECT * FROM itemdata WHERE price > $p");
				if($result) {
					while($row = mysql_fetch_array($result)){
						echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review']."@";
					}				
				}

			} 
			if (strpos($_POST['input'], '<') !== false) {
				$p = str_replace("<","",$_POST['input']);
				$result = mysql_query("SELECT * FROM itemdata WHERE price < $p");
				if($result) {
					while($row = mysql_fetch_array($result)){
						echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review']."@";
					}				
				}
			} 

			else {
				$result = mysql_query("SELECT * FROM itemdata WHERE price = '{$_POST['input']}' OR name LIKE '%{$_POST['input']}%' OR location LIKE '%{$_POST['input']}%'");
				if($result) {
					while($row = mysql_fetch_array($result)){
						echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review']."@";
					}				
				}			
			}
		}
	}
?>