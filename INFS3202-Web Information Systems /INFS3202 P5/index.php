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
			$json = array('id'=>$row['id'],'category'=>$row['category'],'name'=>$row['name'],
				'price'=>$row['price'],'dueTime'=>$row['dueTime'],'location'=>$row['location'],
				'description'=>$row['description'],'imageLink'=>$row['imageLink'],'comment'=>$row['comment']);

			array_push($matches, json_encode($json));

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
		<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
		<link rel="stylesheet" href="css/bootstrap.min.css">

		<script>
			$(function() {
		    	$( ".tabs" ).tabs();

				$( ".add" )
					.button()
			      	.click(function() {	
			      		var ID = $(this).attr("id");
			      		if($("#"+ID+" .input").val()!=""){
							$.ajax({
								type: "POST",
								url: "edit.php",
								data: {func:'addcomment',id:ID,input:$("#"+ID+" .coms").text()+' '+$("#"+ID+" .input").val()+';'},
								success: function (data) {
									$("#"+ID+".result").append(
										"<li class='list-group-item'>"+$("#"+ID+" .input").val()+"</li>"
									)
									$("#"+ID+" .input").val("");
									$("#"+ID+" .coms").text(data);
								}
							});
									      			
			      		}		      								
			    });	

				$( ".cancel" )
					.button()
			      	.click(function() {		        
						$( ".input" ).val("");
			    });		    	


			});

		</script>		

	</head>
	<body>
		<div style="float:left; margin-left:60px; font-size:20px; margin-top:20px;">
			<a href="Admin.php">Admin page</a>
			<a href="search.php" style="margin-left:30px;">Search page</a>
		</div>

		<div style="float:left; clear:left;">
			<?php
				foreach ($matches as $record) {
					

					$id = json_decode($record)->id;
					$category = json_decode($record)->category;
					$name = json_decode($record)->name;
					$price = json_decode($record)->price;
					$dueTime = json_decode($record)->dueTime;
					$location = json_decode($record)->location;
					$description = json_decode($record)->description;
					$imageLink = json_decode($record)->imageLink;
					$comment = json_decode($record)->comment;
					$eachcomment = explode(";",$comment);
					$num = intval(count($eachcomment))-1;
					$i=0;

					echo "<div id='$id' style='float:left; clear:left; margin-left:60px;'>";
					echo "<br><br><br><strong>Item:</strong><br>Category:&nbsp$category<br>";
					echo "$name &nbsp&nbsp&nbsp $$price &nbsp&nbsp&nbsp $dueTime<br>";
					echo "<img src='$imageLink' class='img-thumbnail' style='width:200px; height:150px;'><br><br>";
					echo "Location:<br>$location<br><br>";
					echo "<div class='tabs' style='width:600px;'>
						<ul><li><a href='#tab-1'>description</a></li>
						<li><a href='#tab-2'>comment</a></li></ul>
						<div id='tab-1'>$description</div>
						<div id='tab-2'><ul id='$id' class='list-group result'>";
					for ($i=0; $i < $num; $i++) { 
						echo "<li class='list-group-item'>{$eachcomment[$i]}</li>";
					}
					echo "</ul><textarea class='form-control input' rows='3'></textarea><br>
						<div style='margin-left:310px;'>
						<button id='$id' class='add' type='button'>Add Comment</button>
						<button id='$id' class='cancel' type='button'>Cancel</button>
						<p class='coms' style='display:none'>$comment</p>
						</div></div></div><br><br></div>";
				}
			?>
		</div>

	</body>
</html>