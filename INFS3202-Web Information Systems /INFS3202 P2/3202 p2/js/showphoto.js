
function showphoto(n) {
	if(n==0) {
		document.getElementById("g1").style.display = "block";
		document.getElementById("g2").style.display = "block";
	}
	if(n==1) {
		document.getElementById("g1").style.display = "block";
		document.getElementById("g2").style.display = "none";
	}
	
	if(n==2) {
		document.getElementById("g2").style.display = "block";
		document.getElementById("g1").style.display = "none";
	}
}