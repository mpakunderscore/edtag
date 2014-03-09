function howto() {
	$('#answer').html('<p>It seems like you only know how to <a href="javascript:void(0)" onclick="howto2()">click</a>.</p>');
}

function howto2() {	
	$('#answer').html('<p>Yeah. Sorry. No way.</p>');	
}


function process() {	

	var entered = $("#search").val();

	if (entered === '') alert("Not bad.");
	
	else if ($('#search').attr('type') != 'password' && (/\S+@\S+\.\S+/).test(entered)) { //

        $('#user').data('email', entered);

		changeInput('password', '<p>And password.</p>');
		
	} else if ($('#search').attr('type') === 'password') {

        $.get(
            "/login",
            {email: $('#user').data('email'), password: entered},
            function(data) {

                if (data != "") changeInput('text', data);
                else location.reload();
            }
        );

	} else {
		
		changeInput('text', "<p>Try to say something sensible.</p>");
		
		// words for comands, questions for interactions
	}
}

function changeInput(type, answer) {
	
	$('#search').remove();
	$('#search_wrapper').html('<input id="search" type="' + type + '" />');

	$('#search').focus();
	$('#search').keypress(function(event) {
		if (event.which == 13) {
			process();
		}
	});
	$('#answer').html(answer);
}

$('#search').keypress(function(event) {
    if (event.which == 13) {
        process();
    }
});