function signInCallback(authResult) {
  console.log(JSON.stringify(authResult));
  if (authResult['code']) {

    // Hide the sign-in button now that the user is authorized, for example:
    $('#sign-in-google').attr('style', 'display: none');

    // Send the code to the server
    $.ajax({
      type: 'POST',
      url: '/rest/google-auth',
      // Always include an `X-Requested-With` header in every AJAX request,
      // to protect against CSRF attacks.
      headers: {
        'X-Requested-With': 'XMLHttpRequest'
      },
      contentType: 'application/json; charset=utf-8',
      success: function(result) {
        // Handle or verify the server response.
      },
      processData: false,
      data: JSON.stringify(authResult)
    });
  } else {
    // There was an error.
  }
}

$(function() {
  $('#sign-in-google').click(function() {
    // signInCallback defined in step 6.
    auth2.grantOfflineAccess().then(signInCallback);
  });
  $("#sign-out-google").click(function(){
    console.log("sign out google");
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out.');
    });  
  }); 
});
