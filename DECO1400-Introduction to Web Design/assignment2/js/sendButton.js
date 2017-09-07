//This function check the name is empty or not
function checkName() {
	//get the name from the text field 
	var name=document.getElementById("name").value;
	//when the name is empty, show the error message in the div which id is invalidname and return false
	if(name.length==0) {
		document.getElementById("invalidname").innerHTML="This field is required";
		return false;
	}
	//when the name is not empty, return true
	if(name.length!=0) {
		document.getElementById("invalidname").innerHTML="";
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

//This function is check name, message and email are all correct or not
function send() {
	checkName();
	checkMessage();
	checkEmail();
	//when all are right, show the successful message in the div which id is sendsuccessfully and return true
	if(checkName()==true && checkMessage()==true && checkEmail()==true) {
		document.getElementById("sendsuccessfully").innerHTML="Message successfully sent";
		return true;
	}
	//if one of name, message and email is not right, return false
	return false;
}