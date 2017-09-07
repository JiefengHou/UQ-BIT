// This function is show or hide the div when the user click the button
function showphoto(n) {
	if(n==0) {
		//show the div which id is photo-list and photo-list1
		document.getElementById("photo-list").style.display = "block";
		document.getElementById("photo-list1").style.display = "block";
	}
	if(n==1) {
		//show the div which id is photo-list and hide the div which id is photo-list1
		document.getElementById("photo-list").style.display = "block";
		document.getElementById("photo-list1").style.display = "none";
	}
	
	if(n==2) {
		//hide the div which id is photo-list and show the div which id is photo-list1
		document.getElementById("photo-list1").style.display = "block";
		document.getElementById("photo-list").style.display = "none";
	}
}