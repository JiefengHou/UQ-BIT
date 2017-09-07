<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
	<head>
		<title>INFS3202</title>
		<meta name="description" content="Gallery of place">
		<meta name="keywords" content="INFS3202 Prac4">
		<meta name="author" content="Jiefeng Hou">
		<script src="//code.jquery.com/jquery-1.10.2.js"></script>
		<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
		<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
		<link rel="stylesheet" href="css/bootstrap.min.css">

		<style>
	      html, body, #map-canvas {
	        height: 100%;
	        margin: 0;
	        padding: 0px
	      }

	      #map-canvas {
	        height: 400px;
	        width: 600px;
	        padding: 0px;
	        clear: left;
	        margin-bottom: 30px;
	        float: left;
	      }
    	</style>

		<script>
			$(function() {
				$( "#search" )
					.button()
			      	.click(function() {		        
						$("#result").empty();
						$.ajax({
							type: "POST",
							url: "edit.php",
							data: {input:$("#input").val()},
							success: function (data) {
								var num = data.split("@").length-1;
								if(num == 0) {
									$("#result").append(
										"<strong>Sorry, no results matching your search were found</strong>"
									)
								}
								else {
									for(var i=0; i<num; i++) {
										var record = data.split("@")[i];
										if(i==0){
											$("#result").append(
												"<h3>Result:</h3>"+
												"<div id='map-canvas'></div>"
											)	
											loadScript();								
										}
										$("#result").append(
											"<strong>Item:</strong><br>Category:&nbsp"+record.split("  ")[0]+"<br>" 
											+record.split("  ")[1]+"&nbsp&nbsp&nbsp$"
											+record.split("  ")[2]+"&nbsp&nbsp&nbsp"
											+record.split("  ")[3]+"<br>" 
											+"<img class='img-thumbnail' style='width:200px; height:150px;' src='" +record.split("  ")[6]+"'>"+"<br>"
											+"Location:<br>"
											+record.split("  ")[4]+"<br><br>"
											+"Description:<br>"
											+record.split("  ")[5]+"<br><br>"
											+"Review:<br>"
											+record.split("  ")[7]+"<br><br><br>"
										)
									}
								}								
							}
						});
			    });
			});
		</script>

				<script>
		function initialize() {
		  var mapOptions = {
		    zoom: 12,
		    center: new google.maps.LatLng(-27.47101, 153.02345)
		  };

		  var map = new google.maps.Map(document.getElementById('map-canvas'),
		      mapOptions);

	
			$.ajax({
				type: "POST",
				url: "edit.php",
				data: {input:$("#input").val()},
				success: function (data) {
					var num = data.split("@").length-1;
					for(var i=0; i<num; i++) {
						var record = data.split("@")[i];
						var latLng = new google.maps.LatLng(parseFloat(record.split("  ")[8]), parseFloat(record.split("  ")[9]));
		            	var marker = new google.maps.Marker({
			                position: latLng,
			                map: map
			            });
					}
					
				}
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

	</head>
	<body>
		<div id="qq">
			<h1>Search:</h1>
			<div>
				<input id="input" type="text" class="form-control" placeholder="Text input">
			</div>
			<br>
			<div style="margin-left:520px;">
				<button id="search" type="button" class="btn btn-primary">Search</button>
			</div>

			

			<div id="result" style="float:left;">

			</div>		
		
		</div>
	</body>
</html>