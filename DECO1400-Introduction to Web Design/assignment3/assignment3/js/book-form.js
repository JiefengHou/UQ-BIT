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

//This function check the phone number is empty or not
function checkPhone() {
	//get the phone from the text field 
	var phone=document.getElementById("phoneNumber").value;
	//when the phone is empty, show the error message in the div which id is invalidphone and return false
	if(phone.length==0) {
		document.getElementById("invalidphone").innerHTML="This field is required";
		return false;
	}
	//when the phone is not empty, check the phone format
	if(phone.length!=0) {
		//when the phone number is not numbers, show the error message in the div which id is invalidphone and return false
		if(!/^[\+]?[0-9]+$/.test(phone)) {
			document.getElementById("invalidphone").innerHTML="Please enter a valid phone number";
			return false;
		}
		//when the phone number is right, return true
		else {
			document.getElementById("invalidphone").innerHTML="";
			return true;
		}
	}
}

//This function check the pick up is empty or not
function checkPickUp() {
	//get the pick up from the text field 
	var pickUp=document.getElementById("pickUp").value;
	//when the pickup is empty, show the error message in the div which id is invalidpickup and return false
	if(pickUp.length==0) {
		document.getElementById("invalidpickup").innerHTML="This field is required";
		return false;
	}
	//when the pickup is not empty, return true
	if(pickUp.length!=0) {
		document.getElementById("invalidpickup").innerHTML="";
		return true;
	}
}

//This function check the DropOff is empty or not
function checkDropOff() {
	//get the DropOff from the text field 
	var dropOff=document.getElementById("dropOff").value;
	//when the DropOff is empty, show the error message in the div which id is invalidDropOff and return false
	if(dropOff.length==0) {
		document.getElementById("invaliddropoff").innerHTML="This field is required";
		return false;
	}
	//when the DropOff is not empty, return true
	if(dropOff.length!=0) {
		document.getElementById("invaliddropoff").innerHTML="";
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
	checkPhone();
	checkEmail();
	checkPickUp();
	checkDropOff();
	checkCarName();
	//when all are right, show the successful message in the div which id is sendsuccessfully and return true
	if(checkFname()==true && checkLname()==true && checkEmail()==true && checkPhone()==true && checkPickUp()==true && checkDropOff()==true && checkCarName()==true) {
		return true;
	}
	//if one of name, message and email is not right, return false
	return false;
}