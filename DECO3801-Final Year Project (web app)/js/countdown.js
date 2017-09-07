/*Define redirect and count variables */
var count = 4;
var redirect = "login.php";

/*
 * This function counts down from count seconds and displays this on screen in
 * real time, when 0 is reached, it redirects to a page of the programmers
 * specification
 */

function countDown() {
	/* Create the timer variable and check if count is greater than 0 */
	var timer = document.getElementById("timer");
	if (count > 0) {
		count--;
		timer.innerHTML = "Redirecting to login page in " + count + " seconds.";
		setTimeout("countDown()", 1000);
	} else {
		window.location.href = redirect;
	}
}