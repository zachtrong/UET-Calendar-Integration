function onSuccess(googleUser) {
  console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
  fetch('/rest/google-auth', {
    method: 'post',
    body: JSON.stringify({'id_token': googleUser.getAuthResponse().id_token}),
    headers: {
      'Content-Type': 'application/json'
    }
  }).then(function(response) {
    console.log(response.json());
  });
}
function onFailure(error) {
  console.log(error);
}
function renderButton() {
  console.log("render google button");
  gapi.signin2.render('g-signin2', {
    'scope': 'https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/gmail.readonly profile',
    'width': 240,
    'height': 50,
    'longtitle': true,
    'theme': 'dark',
    'onsuccess': onSuccess,
    'onfailure': onFailure
  });
}

function signOutGoogle() {
    console.log("sign out google");
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out.');
    });
}

$(function() {
  renderButton();
  $("#sign-out-google").click(function(){
    signOutGoogle();
  }); 
});
