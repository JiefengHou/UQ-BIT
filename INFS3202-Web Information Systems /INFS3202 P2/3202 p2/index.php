<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
	<head>
		<title>INFS3202</title>
		<meta name="description" content="Gallery of place">
		<meta name="keywords" content="INFS3202 Prac1">
		<meta name="author" content="Jiefeng Hou">
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<script type="text/Javascript" src="js/showphoto.js"></script>
		<script type="text/javascript">
			var leftTime;

			function counter(time,name)
			{	
				var timer = time;
				var hour = Math.floor(time/3600);
				time %= 3600;
				var min = Math.floor(time/60);
				time %= 60;
				var sec = time;
				sec--;
				
				if (sec < 0) {
					
					if ((min > 0) || (hour > 0)) {
						sec = 59;
						min--;
					}
				}

				if (min < 0) {
					if (hour > 0) {
						min = 59;
						hour--;
					}
				}

				if(name == "title")
				{
					if(timer>0)
					{
						var clocktext = "Time out " + hour + ":" + ((min < 10) ? "0" : "") + min + ":" + ((sec < 10) ? "0" : "") + sec;
						document.title = clocktext;	
						timer--;
						window.setTimeout("counter("+timer+",'" + name + "')", 1000);
					}

					else
					{
						document.title = "Time out " + "0:00:00";
						window.location.href = window.location.href;
					} 
						
				}

				else 
				{
					if(timer>0)
					{
						var clocktext = "Time out " + hour + ":" + ((min < 10) ? "0" : "") + min + ":" + ((sec < 10) ? "0" : "") + sec;
						document.getElementById(name).innerHTML  = clocktext;	
						timer--;
						window.setTimeout("counter("+timer+",'" + name + "')", 1000);
					}	
					
					else document.getElementById(name).innerHTML  = "Time out " + "0:00:00";		
				}
			}

			function startCounter()
			{
				if(document.getElementById("signin").value == "Logout")
				{
					counter(leftTime,'place1');
				}
			}

			function getTime(time)
			{
				var endTime = new Date("2014/4/7,22:00:00");
				leftTime = endTime.getTime()/1000 - time;
				return leftTime;
			}

		</script>

		<?php 
			session_start(); 
			
			if(isset($_SESSION['username']) && isset($_SESSION['password'])){ 
	
				$file = fopen("/tmp/log.txt", "a");
				
				if ($_SESSION['state'] == "login")
				{
					fwrite($file, date("Y-m-d H:i:s", time()) ." ".$_SESSION['username'] ." Login"."\r\n");
				}
				
				$_SESSION['state'] = "logout";
				$serverTime = time();
				$state = "Logout";
				$time = $_SESSION['timeout'] - time() ;	
				echo "<script type='text/javascript'>counter($time,'title');</script>";
				echo "<script type='text/javascript'>getTime($serverTime);</script>";

				if($_SESSION['timeout'] < time()) {	
					fwrite($file, date("Y-m-d H:i:s", time()) ." ".$_SESSION['username'] ." Logout" ." timer" ."\r\n");
					session_destroy();
					header("Location: loginform.php");	
				}
				fclose($file);
			}

			else $state = "Login";
		?>

	</head>
	<body onload="startCounter()">
		<div id="login">
			<input type="button" id="signin" value="<?php echo $state;?>" onclick="location = 'loginform.php' ">
		</div>

		<div id="button">
			<input type="button" value="All" onclick="showphoto(0)">
			<input type="button" value="Restaurants" onclick="showphoto(1)">
			<input type="button" value="Cinemas" onclick="showphoto(2)">
		</div>

	

		<div id="g1">
			<h1>Restaurants</h1>
			<ul>
				<li><img src="images/photo2.jpg" width="150px" height="150px" alt="photo1"><p>Address:197-201 Beaudesert Rd, Moorooka QLD 4105, Phone:3848 6759, Business Hours: 07:00 - 15:00</p><div class="place" id="place1"></div></li>

				<li><img src="images/photo2.jpg" width="150px" height="150px" alt="photo2"><p>Address:197-201 Beaudesert Rd, Moorooka QLD 4105, Phone:3848 6759, Business Hours: 07:00 - 15:00</p><div class="place" id="place2"></div></li>

				<li class="hide"><img src="images/photo3.jpg" width="150px" height="150px" alt="photo3"><p>Address:197-201 Beaudesert Rd, Moorooka QLD 4105, Phone:3848 6759, Business Hours: 07:00 - 15:00</p></li>

				<li class="hide"><img src="images/photo4.jpg" width="150px" height="150px" alt="photo4"><p>Address:197-201 Beaudesert Rd, Moorooka QLD 4105, Phone:3848 6759, Business Hours: 07:00 - 15:00</p></li>
			</ul>
			<a href="#" onclick="window.open('Restaurants.php','_blank')">Show more...</a>
		</div>
		

		<div id="g2">
			<h1>Cinemas</h1>
			<ul>
				<li><img src="images/photo5.jpg" width="150px" height="150px" alt="5hoto5"><p>Address:197-201 Beaudesert Rd, Moorooka QLD 4105, Phone:3848 6759, Business Hours: 07:00 - 15:00</p><div class="place" id="place3"></div></li>

				<li><img src="images/photo6.jpg" width="150px" height="150px" alt="photo6"><p>Address:197-201 Beaudesert Rd, Moorooka QLD 4105, Phone:3848 6759, Business Hours: 07:00 - 15:00</p><div class="place" id="place4"></div></li>

				<li class="hide"><img src="images/photo7.jpg" width="150px" height="150px" alt="photo7"><p>Address:197-201 Beaudesert Rd, Moorooka QLD 4105, Phone:3848 6759, Business Hours: 07:00 - 15:00</p></li>
			</ul>
			<a href="#" onclick="window.open('Cinemas.php','_blank')">Show more...</a>
		</div>
		
	</body>
</html>