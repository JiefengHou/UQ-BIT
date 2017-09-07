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
				'description'=>$row['description'],'imageLink'=>$row['imageLink'],'comment'=>$row['comment'],
				'Latitude'=>$row['Latitude'],'Longitude'=>$row['Longitude']);

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
		<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>

		<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
		<link rel="stylesheet" href="css/bootstrap.min.css">

		<style>
	      html, body, #map-canvas,.mapdirection {
	        height: 100%;
	        margin: 0;
	        padding: 0px
	      }

	      #map-canvas {
	        height: 400px;
	        width: 600px;
	        margin-left: 60px;
	        padding: 0px;
	        clear: left;
	        margin-top: 30px;
	        float: left;
	      }

	      .mapdirection
	      {
	        height: 400px;
	        width: 550px;
	      }
    	</style>

		<script>
			$(function() {
		    	$( ".tabs" ).tabs({
		    		activate: function(event,ui) {

				       if ( ui.newPanel.selector=="#tab-3" ) {
				        	var id = $(this).attr("id");
				        	var dlat = $("#map-"+id+" .map-dlat").text();
				        	var dlon = $("#map-"+id+" .map-dlon").text();				        		        	
				        	adddirection(id,dlat,dlon); 	
				        }
				        
				    }
			    });


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


		<script>
			function initialize() {
			  var mapOptions = {
			    zoom: 8,
			    center: new google.maps.LatLng(-27.62832, 152.75515)
			  };

			  var map = new google.maps.Map(document.getElementById('map-canvas'),
			      mapOptions);

				$( ".addmaker" )
					.button()
			      	.click(function() {	
			      	var ID = $(this).attr("id");	
					$.ajax({
						type: "POST",
						url: "edit.php",
						data: {func:'addmaker',id:ID},
						success: function (data) {
							var latLng = new google.maps.LatLng(parseFloat(data.split("  ")[0]), parseFloat(data.split("  ")[1]));
				            var marker = new google.maps.Marker({
				                position: latLng,
				                map: new google.maps.Map(document.getElementById('map-canvas'),{center:latLng,zoom:15}),
				            });
						}
					});	        		
			    });			  
			}

			function loadScript() {
			  var script = document.createElement('script');
			  script.type = 'text/javascript';
			  script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&' +
			      'callback=initialize';
			  document.body.appendChild(script);
			}

		window.onload = loadScript;

    </script>	


    <script>
    	var clat,clon;

    	function getLocation(){
			if (navigator.geolocation){
			    navigator.geolocation.getCurrentPosition(showPosition);
			} 
		}

		function showPosition(position){
			clat=position.coords.latitude;
			clon=position.coords.longitude;
		}

    	function adddirection(id,dlat,dlon){
    		var cPosition = new google.maps.LatLng(clat,clon);
    		var dPosition = new google.maps.LatLng(dlat,dlon);
    		
    		directionsDisplay = new google.maps.DirectionsRenderer(); 
            directionsService = new google.maps.DirectionsService();

    		var mapOptions = {
			    zoom: 16,
			    center: cPosition,
			    mapTypeId: google.maps.MapTypeId.ROADMAP
			};

		  	var map = new google.maps.Map(document.getElementById("map-"+id),
		    	mapOptions);

		  	directionsDisplay.setMap(map);

		  	var currentPositionMarker = new google.maps.Marker({
                    position: cPosition,
                    map: map,
                    title: "Current position"
            });

		  	calculateRoute(cPosition,dPosition);
    	}

    	function calculateRoute(cPosition,dPosition) {        
            if (cPosition != '' && dPosition != '') {

                var request = {
                    origin: cPosition, 
                    destination: dPosition,
                    travelMode: google.maps.DirectionsTravelMode["DRIVING"]
                };

                directionsService.route(request, function(response, status) {
                    if (status == google.maps.DirectionsStatus.OK) {
                        directionsDisplay.setDirections(response);
                    }
                });
            }  
        }
    </script>

    

	</head>
	<body onload="getLocation();">
		
		<div style="float:left; margin-left:60px; font-size:20px; margin-top:20px;">
			<a href="Admin.php">Admin page</a>
			<a href="search.php" style="margin-left:30px;">Search page</a>
		</div>

		<div id="map-canvas"></div>
		

		

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
					$Latitude = json_decode($record)->Latitude;
					$Longitude = json_decode($record)->Longitude;
					$eachcomment = explode(";",$comment);
					$num = intval(count($eachcomment))-1;
					$i=0;

					echo "<div id='$id' style='float:left; clear:left; margin-left:60px;'>";
					echo "<br><br><br><strong>Item:</strong><br>Category:&nbsp$category<br>";
					echo "$name &nbsp&nbsp&nbsp $$price &nbsp&nbsp&nbsp $dueTime<br>";
					echo "<img src='$imageLink' class='img-thumbnail' style='width:200px; height:150px;'><br><br>";
					echo "<button  id='$id' class='addmaker'>Location</button><br>";
					echo "Location:<br>$location<br><br>";
					echo "<div id='$id' class='tabs' style='width:600px;'>
						<ul><li><a href='#tab-1'>description</a></li>
						<li><a href='#tab-2'>comment</a></li>
						<li><a href='#tab-3'>direction</a></li></ul>
						<div id='tab-1'>$description</div>
						<div id='tab-3'><div id='map-$id' class='mapdirection'>
						<p class='map-dlat' style='display:none'>$Latitude</p>
						<p class='map-dlon' style='display:none'>$Longitude</p></div></div>
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