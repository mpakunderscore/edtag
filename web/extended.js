$('#search').keypress(function(event) {
    if (event.which == 13) {
        process2();
    }
});


function process2() {

    var entered = $("#search").val();

    if (entered === '') return;

    query(entered);

    $('#search').val('');

    $('#answer').html(entered);
}

function query(text) {

    $.get("/query", {text: text}, function( data ) {});
}

function list() {

	$.getJSON("get.json",	
	
    // $.get(
    //     "/get",
    //     {},	
        function(data) {

            $('#main').html('');
            $('#main').append('<div id="out"><table id="data"></table></div>');

            for (var id in data) {

                var row = "<tr>" +					
					// "<td>" + data[id]['url'].replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0] + "</td>" +
                    "<td><img src='"+ data[id]['faviconurl'] + "' height='15' width='15'></td>" +
                    "<td class='study'><a href='" + data[id]['url'] + "'>" + data[id]['title'].replace("\<", "\<\\") + "</a><a><font color='green'>" + Object.keys(JSON.parse(data[id]['usertags'])).length + "</font></a><a><font color='gray'>" + Object.keys(JSON.parse(data[id]['tags'])).length + "</font></a> </td>" +
                    "<td>" + Object.keys(JSON.parse(data[id]['usertags'])).join(", ") + "</td>" +
                    // "<td>" + Object.keys(JSON.parse(data[id]['tags'])).slice(0, 15).join(", ") + ", ...</td>" + 
					"</tr>";

                $('#data').append(row);
            }
			
            for (var id in data) {

                var row = "<tr>" +					
					// "<td>" + data[id]['url'].replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0] + "</td>" +
                    "<td><img src='"+ data[id]['faviconurl'] + "' height='15' width='15'></td>" +
                    "<td class='study'><a href='" + data[id]['url'] + "'>" + data[id]['title'].replace("\<", "\<\\") + "</a></td>" +
                    "<td>" + Object.keys(JSON.parse(data[id]['usertags'])).join(", ") + "</td>" +
                    // "<td>" + Object.keys(JSON.parse(data[id]['tags'])).slice(0, 15).join(", ") + ", ...</td>" + 
					"</tr>";

                $('#data').append(row);
            }		        
    });
	
	$.getJSON("domains.json",
	
    // $.get(
    //     "/get",
    //     {},	
        function(data) {
	
            $('#main').append('<table id="datasort" border="0"></table>');

            for (var id in data) {

                var row = "<tr>" +					
					// "<td>" + data[id]['url'].replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0] + "</td>" +
                    "<td><a href='#'><img src='"+ data[id]['faviconurl'] + "' height='15' width='15'></a></td>" +
                    // "<td><a href='" + data[id]['url'] + "'>" + data[id]['title'].replace("\<", "\<\\") + "</a></td>" +
                    // "<td>" + Object.keys(JSON.parse(data[id]['usertags'])).join(", ") + "</td>" +
                    // "<td>" + Object.keys(JSON.parse(data[id]['tags'])).slice(0, 15).join(", ") + ", ...</td>" + 
					"</tr>";

                $('#datasort').append(row);
            }			        
    });
	
	$.getJSON("tags.json",
	
    // $.get(
    //     "/get",
    //     {},	
        function(data) {
	
            $('#main').append('<table id="tagsdatasort" border="0"></table>');

            for (var id in data) {

                var row = "<tr>" +					
                    "<td><a href='#'>"+ data[id]['tag'] + "</a></td>" +
					"</tr>";

                $('#tagsdatasort').append(row);
            }			        
    });
}

function tags() {

    $('#main').html('');
}

list();