function signInCallback(authResult) {

    if (authResult['status']['signed_in']) {
        // Update the app to reflect a signed in user
        // Hide the sign-in button now that the user is authorized, for example:

        document.getElementById('signoutButton').setAttribute('style', 'display: inline');
        document.getElementById('signinButton').setAttribute('style', 'display: none');

    } else {

        // Update the app to reflect a signed out user
        // Possible error values:
        //   "user_signed_out" - User is signed-out
        //   "access_denied" - User denied access to your app
        //   "immediate_failed" - Could not automatically log in the user

        document.getElementById('signinButton').setAttribute('style', 'display: inline');
        document.getElementById('signoutButton').setAttribute('style', 'display: none');
        console.log('Sign-in state: ' + authResult['error']);
    }

    if (authResult['code']) {

        // Hide the sign-in button now that the user is authorized, for example:
        $('#signinButton').attr('style', 'display: none');

        // Send the code to the server
        $.ajax({
            type: 'POST',
            url: '/plus?storeToken',
            contentType: 'application/octet-stream; charset=utf-8',
            success: function(result) {
                // Handle or verify the server response if necessary.

                // Prints the list of people that the user has allowed the app to know
                // to the console.
                console.log(result);
                if (result['profile'] && result['people']){
                    $('#results').html('Hello ' + result['profile']['displayName'] + '. You successfully made a server side call to people.get and people.list');
                } else {
                    $('#results').html('Failed to make a server-side call. Check your configuration and console.');
                }
            },
            processData: false,
            data: authResult['code']
        });
    } else if (authResult['error']) {
        // There was an error.
        // Possible error codes:
        //   "access_denied" - User denied access to your app
        //   "immediate_failed" - Could not automatially log in the user
        // console.log('There was an error: ' + authResult['error']);
    }
}