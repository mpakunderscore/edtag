function signInCallback(authResult) {

    if (authResult['status']['signed_in']) {


        document.getElementById('signOutButton').setAttribute('style', 'display: inline');
        document.getElementById('signInButton').setAttribute('style', 'display: none');

        $.post("/auth", { code: authResult['code'] });

        console.log(authResult);

        pages();

        return;

//        $.ajax({
//            type: 'POST',
//            url: '/signInCallback',
//            contentType: 'application/octet-stream; charset=utf-8',
//            processData: true,
//            data: authResult['code'],
//            success: function(result) {
//
//                console.log(result);
//
//                if (result['profile']){
//
//
//
//                } else {
//
//                    $('#main').html('Failed to make a server-side call. Check your configuration and console.');
//                }
//            }
//        });


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

    global();
}

var fixmeTop = 50;                              // Get initial position
$(window).scroll(function() {                   // Assign scroll event listener

    var currentScroll = $(window).scrollTop();  // Get current position
    if (currentScroll >= fixmeTop) {            // Make it fixed if you've scrolled to it

        $('#domains').css({
            position: 'fixed',
            top: '20px',
            left: '20px'
        });

        $('#tags').css({
            position: 'fixed',
            top: '20px',
            left: '90px'
        });


    } else {

        $('#domains').css({
            position: 'absolute',
            top: '70px',
            left: '20px'
        });

        $('#tags').css({
            position: 'absolute',
            top: '70px',
            left: '90px'
        });
    }
});