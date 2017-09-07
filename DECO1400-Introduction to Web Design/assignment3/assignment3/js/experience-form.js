//This function check the first name is empty or not
function checkFname() {
	//get the first name from the text field 
	var fname=document.getElementById("fname").value;
	//when the fisrt name is empty, show the error message in the div which id is invalidfname and return false
	if(fname.length==0) {
		document.getElementById("invalidfname").innerHTML="This field is required";
		return false;
	}
	//when the first name is not empty, return true
	if(fname.length!=0) {
		document.getElementById("invalidfname").innerHTML="";
		return true;
	}
}

//This function check the last name is empty or not
function checkLname() {
	//get the last name from the text field 
	var lname=document.getElementById("lname").value;
	//when the last name is empty, show the error message in the div which id is invalidlname and return false
	if(lname.length==0) {
		document.getElementById("invalidlname").innerHTML="This field is required";
		return false;
	}
	//when the last name is not empty, return true
	if(lname.length!=0) {
		document.getElementById("invalidlname").innerHTML="";
		return true;
	}
}

//This function check the message is empty or not
function checkMessage(){
	//get the message from the text field 
	var message=document.getElementById("message").value;
	//when the message is empty, show the error message in the div which id is invalidmessage and return false
	if(message.length==0) {
		document.getElementById("invalidmessage").innerHTML="This field is required";
		return false;
	}
	//when the message is not empty, return true
	if(message.length!=0) {
		document.getElementById("invalidmessage").innerHTML="";
		return true;
	}
}

//This function check the email format and is empty or not
function checkEmail() {
	//get the email from the text field 
	var email=document.getElementById("email").value;
	//when the email is empty, show the error message in the div which id is invalidemail and return false
	if(email.length==0) {
		document.getElementById("invalidemail").innerHTML="This field is required";
		return false;
	}	
	//when the email is not empty, check the email format
	if(email.length!=0) {
		document.getElementById("invalidemail").innerHTML="";
		/* 	check the email format, if it is a valid address which should contain a single @ symbol with at least 2 
			characters before it, and contain a single . with at least 2 characters after it. 
			The . must be after the @.  When the format is wrong, return false and show the error message in the div 
			which id is invalidemail*/
		if(!/^([0-9a-zA-Z\-]{2,})+@([0-9A-Za-z])+\.([0-9a-zA-Z]{2,})+$/.test(email)) {
			document.getElementById("invalidemail").innerHTML="Please enter a valid email address";
			return false;
		}
		//when the email format is right, return true
		if(/^([0-9a-zA-Z\-]{2,})+@([0-9A-Za-z])+\.([0-9a-zA-Z]{2,})+$/.test(email)) {
			document.getElementById("invalidemail").innerHTML="";
			return true;
		}
	}		
}

//This function check the subject is empty or not
function checkSubject() {
	//get the subject from the text field 
	var subject=document.getElementById("subject").value;
	//when the subject is empty, show the error message in the div which id is invalidsubject and return false
	if(subject.length==0) {
		document.getElementById("invalidsubject").innerHTML="This field is required";
		return false;
	}
	//when the subject is not empty, return true
	if(subject.length!=0) {
		document.getElementById("invalidsubject").innerHTML="";
		return true;
	}	
}

//This function check the car name select or not
function checkCarName() {
	//get the index from "caeName"
	var selectIndex=document.getElementById("carName").selectedIndex;
		//if the index is 0, it means user dose not select the car, how the error message in the div which 
		//id is invalidcarname and return false
		if(selectIndex==0) {
			document.getElementById("invalidcarname").innerHTML="This field is required";
			return false;
		}
		//if the index is not 0, return true
		if(selectIndex!=0) {
			document.getElementById("invalidcarname").innerHTML="";
			return true;			
		}
}

//This function is check name, message and email are all correct or not
function send() {
	checkFname();
	checkLname();
	checkMessage();
	checkEmail();
	checkSubject();
	checkCarName();
	//when all are right, show the successful message in the div which id is sendsuccessfully and return true
	if(checkFname()==true && checkLname()==true && checkMessage()==true && checkEmail()==true && checkSubject()==true && checkCarName()==true) {
		return true;
	}
	//if one of name, message and email is not right, return false
	return false;
}