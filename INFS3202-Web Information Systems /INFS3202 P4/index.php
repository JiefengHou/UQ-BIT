<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<?php
	
	$username="admin";
	$userpass="infs3202";
	$dbhost="localhost";
	$dbdatabase="infs3202";
	$matches = array();

	$db_connect = mysql_connect($dbhost,$username,$userpass) or die("Unable to connect to MySQL");

	$selected = mysql_select_db($dbdatabase,$db_connect) or die("Could not select infs3202");

	$result = mysql_query("SELECT * FROM itemdata");

	if($result){
		while($row = mysql_fetch_array($result)){
			array_push($matches, $row['id']."  ".$row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".
				$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review']);

		}
	} else {
		die("There is no data");
	}
?>

<html>
	<head>
		<title>INFS3202</title>
		<meta name="description" content="Gallery of place">
		<meta name="keywords" content="INFS3202 Prac4">
		<meta name="author" content="Jiefeng Hou">
		<script src="//code.jquery.com/jquery-1.10.2.js"></script>
		<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
		<link rel="stylesheet" href="css/bootstrap.min.css">
		

	</head>
	<body>
		<div style="float:left; margin-left:60px; font-size:20px; margin-top:20px;">
			<a href="Admin.php">Admin page</a>
			<a href="search.php" style="margin-left:30px;">Search page</a>
		</div>

		<div style="float:left; clear:left;">
			<?php
				foreach($matches as $item) {
					
					$id = explode("  ",$item)[0];
					$category = explode("  ",$item)[1];
					$name = explode("  ",$item)[2];
					$price = explode("  ",$item)[3];
					$dueTime = explode("  ",$item)[4];
					$location = explode("  ",$item)[5];
					$description = explode("  ",$item)[6];
					$imageLink = explode("  ",$item)[7];
					$review = explode("  ",$item)[8];
					
					echo "<div style='float:left; clear:left; margin-left:60px;'>";
					echo "<br><br><br><strong>Item:</strong><br>Category:&nbsp$category<br>";
					echo "$name &nbsp&nbsp&nbsp $$price &nbsp&nbsp&nbsp $dueTime<br>";
					echo "<img src='$imageLink' class='img-thumbnail' style='width:200px; height:150px;'><br><br>";
					echo "Location:<br>$location<br><br>";
					echo "Description:<br>$description<br><br>";
					echo "Review:<br>$review";
					echo "</div>";
				}
			?>			
		</div>
		
	</body>
</html>