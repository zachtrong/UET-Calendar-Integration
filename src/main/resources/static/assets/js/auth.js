$(function() {
  $('#uet-auth-form').on("submit", function(e) {
    e.preventDefault(); // cancel the actual submit
    // Should be triggered on form submit

    var uetCreds = "username=" + $('#uet-username').val() + "&password=" + $('#uet-password').val();
    
    fetch('https://api.github.com/gists', {
      method: 'post',
      body: uetCreds
    }).then(function(response) {
      console.log(response.json());
    });
  });
});