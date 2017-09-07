function title_time(time)
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

	if(timer>0)
	{
		var clocktext = "Time out " + hour + ":" + ((min < 10) ? "0" : "") + min + ":" + ((sec < 10) ? "0" : "") + sec;
		document.title = clocktext;	
		timer--;
		window.setTimeout("title_time("+timer+")", 1000);
	}

	else document.title = "Time out " + "0:00:00";
}