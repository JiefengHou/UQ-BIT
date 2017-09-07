// This function is show div when the user click the button
function showInfor(n) {
	if(n==1) {
		//show the the "car1-info" div 
		document.getElementById("car1-info").style.display = "block";
	}

	if(n==2) {
		//show the the "car2-info" div 
		document.getElementById("car2-info").style.display = "block";
	}

	if(n==3) {
		//show the the "car3-info" div 
		document.getElementById("car3-info").style.display = "block";
	}
	
	if(n==4) {
		//show the the "car4-info" div 
		document.getElementById("car4-info").style.display = "block";
	}
	
}

// This function is hide div when the user click the button
function hideInfor(n) {
	if(n==1) {
		//hide the the "car1-info" div 
		document.getElementById("car1-info").style.display = "none";
	}
	if(n==2) {
		//hide the the "car2-info" div 
		document.getElementById("car2-info").style.display = "none";
	}
	
	if(n==3) {
		//hide the the "car3-info" div 
		document.getElementById("car3-info").style.display = "none";
	}
	
	if(n==4) {
		//hide the the "car4-info" div 
		document.getElementById("car4-info").style.display = "none";
	}
}
