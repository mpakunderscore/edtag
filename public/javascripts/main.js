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
		
		changeInput('password', '<p>And password.</p>');
		
	} else if ($('#search').attr('type') === 'password') {

		var p1 = "<p>Now it seems that you're not stupid. It is experimental system of self-education that helps you to find a new information through implicit search, save pages in the tree tag and select the best knowledge from different perspectives.</p>"
		var p2 = "<p>You can try to enter any query and see what will happen. There's sort of understandable. Interface is constantly changing and you will likely find something new from time to time. Just ask if you need something.</p>";
		var p3 = "<p>If you want to quickly grow your tag tree - here's a <a href='javascript:void(0)'>plugin</a> to add your favorite pages. Desirable educational.</p>";
		var p4 = "<p>Well. And. Don't tell the idiots about it.</p>"
		
		changeInput('text', p1 + p2 + p3 + p4);
						
	} else {
		
		changeInput('text', "<p>Okay, I give up. Here's a couple of spells, maybe it will help. Or kill you, who knows.</p>");
		
		// ( _0_ _8_  _o_ )  _O_ _U_ _Q_  _C_ _D_  _Y_ _V_ _W_  _*_  heads for players (requests before the game)
		
		// \_/ /~\ |::| [""]
		
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
	$('#answer').show("slow");
}

changeInput('text', '<p>This place is only for those, who know <a href="javascript:void(0)" onclick="howto()">how to</a> enter.</p>');