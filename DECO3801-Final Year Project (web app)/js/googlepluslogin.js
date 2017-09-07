function onSigninCallback(authResult) {
	if (authResult['status']['signed_in']) { // User Signed In
		// Update the app to reflect a signed in user
		// Hide the sign-in button now that the user is authorized, for example:
		document.getElementById('g-signin').setAttribute('style',
				'display: none');

	} else { // User not Signed In
		// Update the app to reflect a signed out user
		// Possible error values:
		// "user_signed_out" - User is signed-out
		// "access_denied" - User denied access to your app
		// "immediate_failed" - Could not automatically log in the user

	}
	console.log('Sign-in state: ' + authResult['error']);
}