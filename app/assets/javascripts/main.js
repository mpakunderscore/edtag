function signInCallback(authResult) {

    if (authResult['status']['signed_in']) {

        document.getElementById('signOutButton').setAttribute('style', 'display: inline');
        document.getElementById('signInButton').setAttribute('style', 'display: none');

        $.ajax({
            type: 'POST',
            url: '/signInCallback',
            contentType: 'application/octet-stream; charset=utf-8',
            success: function(result) {

                console.log(result);

                if (result['profile']){

                    pages();

                } else {

                    $('#main').html('Failed to make a server-side call. Check your configuration and console.');
                }
            },
            processData: true,
            data: authResult['code']
        });


    } else {

        // Update the app to reflect a signed out user
        // Possible error values:
        //   "user_signed_out" - User is signed-out
        //   "access_denied" - User denied access to your app
        //   "immediate_failed" - Could not automatically log in the user

        document.getElementById('signInButton').setAttribute('style', 'display: inline');
        document.getElementById('signOutButton').setAttribute('style', 'display: none');
        console.log('Sign-in state: ' + authResult['error']);
    }
}