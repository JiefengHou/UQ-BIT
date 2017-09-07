var curIndex=0; 
var timeInterval=3000; //set time 
//create a list and put all showing images into it
var arr=new Array();
arr[0]="images/photo1.png";
arr[1]="images/photo2.png";
arr[2]="images/photo3.png";
arr[3]="images/photo4.png";
//set a timing for run function changeImg() every 3s
var i=setInterval(changeImg,timeInterval);

//This function is that to change the image by index
function changeImg() {
	//get the element by id "photo"
	var photo=document.getElementById("photo");
	//if index is the last one, then the index will change back to 0
	if (curIndex==arr.length-1) {
		curIndex=0;
	}
	//if the index is not the last one, then index will plus 1
	else {
		curIndex+=1;
	}
	//chage the src of photo
	photo.src=arr[curIndex];
}

//This function is called when user click the right image
function next() {
	//stop the timing i
	clearInterval(i);
	//get the element by id "photo"
	var photo=document.getElementById("photo");
	//if index is the last one, then the index will change back to 0
	if (curIndex==arr.length-1) {
		curIndex=0;
	}
	//if the index is not the last one, then index will plus 1
	else {
		curIndex+=1;
	}
	//chage the src of photo
	photo.src=arr[curIndex];
	//restart the timging i
	i=setInterval(changeImg,timeInterval);
}

//This function is call when user click the left image
function previous() {
	//stop the timing i
	clearInterval(i);
	//get the element by id "photo"
	var photo=document.getElementById("photo");
	//if index is the first one, then the index will change to last one
	if (curIndex==0) {
		curIndex=arr.length-1;
	}
	//if the index is not the last one, then index will min 1
	else {
		curIndex-=1;
	}
	//chage the src of photo
	photo.src=arr[curIndex];
	//restart the timging i
	i=setInterval(changeImg,timeInterval);
}
