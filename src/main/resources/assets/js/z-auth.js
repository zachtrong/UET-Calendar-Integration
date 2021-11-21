$(function() {
  $('#uet-auth-form').on("submit", function(e) {
    e.preventDefault(); // cancel the actual submit
    // Should be triggered on form submit

    var uetCreds = $("#uet-auth-form").serialize();
    
    fetch('/rest/uet-auth', {
      method: 'post',
      body: uetCreds,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }).then(function(response) {
      console.log(response.json());
    });
  });
});

