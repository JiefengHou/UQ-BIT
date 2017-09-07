<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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
			array_push($matches, $row['id'].",".$row['name']);

		}
	} else {
		die("There is no data");
	}
?>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="keywords" content="INFS3202 Prac4">
	<meta name="author" content="Jiefeng Hou">
	<script src="//code.jquery.com/jquery-1.10.2.js"></script>
	<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<link rel="stylesheet" href="css/jquery-ui.css">
	<title>Admin Page</title>
	  <style>
	    label, input { display:block; }
	    input.text { margin-bottom:12px; width:95%; padding: .4em; }
	    
	    h1 { font-size: 1.2em; margin: .6em 0; }
	    div#users-contain { width: 350px; margin: 20px 0; }
	    div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
	    div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
	    .ui-dialog .ui-state-error { padding: .3em; }
	    .validateTips { border: 1px solid transparent; padding: 0.3em;}
	  </style>

	<script>
		$(function() {
			var itemId = 0;
			var mode = "";
			var deleteID = 0;

			$( "#confirm-form" ).dialog({
				autoOpen: false,
		     	height:160,
		        modal: true,
		        buttons: {
			      	Yes: function() {
			        
			        	$.post("edit.php", {func:"deletedata",id:deleteID},function(data){
							location.reload();
						}); 
			        },

			        No: function() {
			          $( this ).dialog( "close" );
			        }
		        }
		    });


			$( "#data-form" ).dialog({
				autoOpen: false,
		      	height: 630,
		      	width: 400,
		      	modal: true,

			    overlay: {
			        backgroundColor: '#000',
			        opacity: 0.5
			    },

		      	buttons: {
		      		
		      		Update: function() {
		      			if($("#category").val()=="" || $("#name").val()==""){
		      				$( ".validateTips" ).text( "category and name cannot be empty!" );
		      			} else {
			      			if(mode == "add"){
				      			$.post("edit.php", {func:"adddata",category:$("#category").val(),name:$("#name").val(),
				      				price:$("#price").val(),dueTime:$("#dueTime").val(),location:$("#location").val(),
				      				description:$("#description").val(),imageLink:$("#imageLink").val(),comment:$("#comment").val()},function(data){
									location.reload();
					    		}); 
			      			} else {
				      			$.post("edit.php", {func:"updatedata",id:itemID,category:$("#category").val(),name:$("#name").val(),
				      				price:$("#price").val(),dueTime:$("#dueTime").val(),location:$("#location").val(),
				      				description:$("#description").val(),imageLink:$("#imageLink").val(),comment:$("#comment").val()},function(data){
									location.reload();
					    		}); 
			      			}
		      			}
        			},

		      		Cancel: function() {
          				$( this ).dialog( "close" );
        			},
		      	},
			});

			$( "#add" )
		      .button()
		      .click(function() {
			    	mode = "add";
			    	$("#category").val("");
			    	$("#name").val("");
			    	$("#price").val("");
			    	$("#dueTime").val("");
			    	$("#location").val("");
			    	$("#description").val("");
			    	$("#imageLink").val("");
			    	$("#comment").val("");		        
		        $( "#data-form" ).dialog( "open" );
		     });

			$( ".edit" )
		      .click(function() {		        
				itemID = $(this).attr("id");
				mode="edit";
				$.post("edit.php", {func:"getdata",id:$(this).attr("id")},function(data){
			    	$("#category").val(data.split("  ")[0]);
			    	$("#name").val(data.split("  ")[1]);
			    	$("#price").val(data.split("  ")[2]);
			    	$("#dueTime").val(data.split("  ")[3]);
			    	$("#location").val(data.split("  ")[4]);
			    	$("#description").val(data.split("  ")[5]);
			    	$("#imageLink").val(data.split("  ")[6]);
			    	$("#comment").val(data.split("  ")[7]);
			    }); 
		        $( "#data-form" ).dialog( "open" );
		    });

			$( ".remove" )
		      .click(function() {		        
				deleteID = $(this).attr("id");
				$( "#confirm-form" ).dialog( "open" );
		    });
		});
		
	</script>

</head>

<body>
	<div style="width:500px; margin:20px auto;">
		<table id="users" class="table table-striped table-bordered">
			<?php
				foreach($matches as $item) {
					$name = explode(",",$item)[1];
					$id = explode(",",$item)[0];
					
					echo "<tr><td>$name</td><td><a id='$id' class='edit'>Edit</a></td><td><a id='$id' class='remove'>Remove</a></td></tr>";
				}
			?>
		</table>
		<button id="add" type="button" style="float:left; margin-left:440px;">Add</button>
	</div>

	<div id="data-form">
		<p class="validateTips"></p>
		<form>
			
				<label for="category">Category</label>
			    <input type="text" name="category" id="category" class="text ui-widget-content ui-corner-all">
				<label for="name">Name</label>
			    <input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all">
			  	<label for="price">Price</label>
			    <input type="text" name="price" id="price" class="text ui-widget-content ui-corner-all">
			    <label for="dueTime">Due Time</label>
			    <input type="text" name="dueTime" id="dueTime" class="text ui-widget-content ui-corner-all">
			    <label for="location">Location</label>
			    <input type="text" name="location" id="location" class="text ui-widget-content ui-corner-all">
			    <label for="description">Description</label>
			    <input type="text" name="description" id="description" class="text ui-widget-content ui-corner-all">
			    <label for="link">Image Link</label>
			    <input type="text" name="imageLink" id="imageLink" class="text ui-widget-content ui-corner-all">
			    <label for="comment">comment(s)</label>
			    <input type="text" name="comment" id="comment" class="text ui-widget-content ui-corner-all">
			
		</form>
	</div>

	<div id="confirm-form">	
		<form>
			<p>Are you sure?</p>			
		</form>
	</div>
	
</body>
</html>

