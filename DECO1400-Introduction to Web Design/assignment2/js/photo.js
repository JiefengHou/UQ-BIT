//This function show the big photo following the mouse if user across the image
function showPic(sUrl,event){  
     //get the image div which id is show-photo
	 var oSon = document.getElementById("show-photo");  
     if (oSon == null) return;  
     with (oSon){  
		//give the image url into the html
		document.getElementById("show-photo").innerHTML = "<img src=\"" + sUrl + "\">"; 
		//show the div which id is show-photo
		document.getElementById("show-photo").style.display = "block";
		//get the mouse coordinates in the website
		style.pixelLeft = window.event.clientX + window.document.body.scrollLeft + 6;  
		style.pixelTop = window.event.clientY + window.document.body.scrollTop + 9;  
   
     }  
}  

//This funtion hide the big photo if mouse move out of image
function hiddenPic(){  
     //get the image div which id is show-photo
	 var oSon = document.getElementById("show-photo");  
     if(oSon == null) return;  
	 //hide the image in the div which id is show-photo
     document.getElementById("show-photo").innerHTML = "";
	 //hide the div which id is show-photo
	 document.getElementById("show-photo").style.display = "none";  
}