(function() {
	var po = document.createElement('script');
	po.type = 'text/javascript';
	po.async = true;
	po.src = 'https://apis.google.com/js/client:plusone.js';
	var s = document.getElementsByTagName('script')[0];
	s.parentNode.insertBefore(po, s);
})();

function render() {
	// Additional params
	var additionalParams = {
		'callback' : onSigninCallBack
	};

	var signinButton = document.getElementById('signinButton');
	signinButton.addEventListener('click', function() {

		gapi.auth.signIn(additionalParams); // Will use page level configuration
	});

}