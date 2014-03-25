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

	$.getJSON("get.json", {},

    // $.get("/get", {},
	
        function(data) {
			
			//data

            $('#main').html('');
            $('#main').append('<div id="out"><table id="data"></table></div>');
			
			var domains = {};
			var favicons = {}; //TODO

            var tags = {};

            for (var id in data) {
				
				var domain = data[id]['url'].replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
				if (domains[domain] != null) domains[domain]++;
				
				else {
					
					domains[domain] = 1;
					favicons[domain] = data[id]['faviconurl'];
				}

                var url_tags = JSON.parse(data[id]['usertags']);

                for (var tag in url_tags) {

                    if (tags[tag] != null) tags[tag] += parseInt(url_tags[tag]);
					
                    else tags[tag] = parseInt(url_tags[tag]);                    
                }
				
				var title = data[id]['title'].replace("\<", "\<\\");
				
				if (title.length > 100) title = title.substring(0, 110) + "...";
				
				var progress_block = "&#8211;";
				// var progress_block = "&#183;"; //midl dot ··················

                var row = "<tr>" +

                   	// "<td><font color='gray'>9:24 pm</font></td>" +

                    "<td><a href='#'><img src='"+ data[id]['faviconurl'] + "' height='15' width='15'></a></td>" +
                    
					"<td class='study'>" +
					"<a href='" + data[id]['url'] + "' target='_blank'>" + title + "</a>" +
					
					// "<a><font color='green'>" + Object.keys(JSON.parse(data[id]['usertags'])).length + "</font></a>" + 
					// "<a><font color='gray'>" + Object.keys(JSON.parse(data[id]['tags'])).length + "</font></a>" +
					
					"<span title='" + Object.keys(JSON.parse(data[id]['tags'])).length + "'><font color='#00ffff'>" +
					 
					Array(Math.floor(Object.keys(JSON.parse(data[id]['usertags'])).length/5 + 4)).join(progress_block) + "</font>" + 
					
					// Math.floor(
					"<font color='gray'>" + Array(Math.floor(Object.keys(JSON.parse(data[id]['tags'])).length/20)).join(progress_block) + "</font></span>" +
					
					
					
					// Object.keys(JSON.parse(data[id]['usertags'])).join(", ") +
					"</td>" +
					
                    // "<td>" +  "</td>" +
                    // "<td>" + Object.keys(JSON.parse(data[id]['tags'])).slice(0, 15).join(", ") + ", ...</td>" + 
					
					"</tr>";

                $('#data').append(row);
            }	        
			
			$('#data').append("<tr></tr><tr></tr><tr><td></td><td><a href='#'>Load more</a></td></tr><tr></tr><tr></tr>");
			
			//domains
			
            $('#main').append('<table id="domains" border="0"></table>');

            var domains_sort = sort(domains);

            for (var id in domains_sort) {

                var row = "<tr>" +
                    "<td><a href='#'><img src='"+ favicons[domains_sort[id][0]] + "' height='15' width='15' title='" + domains_sort[id][0] + "'></a></td>" +
					"</tr>";

                $('#domains').append(row);
            }

            //tags

            $('#main').append('<table id="tags" border="0"></table>');
			
			$('#tags').append("<tr><td><a href='#'>.base</a></td></tr>");
			$('#tags').append("<tr><td><a href='#'>.target</a></td></tr>");
			$('#tags').append("<tr><td><a href='#'>.video</a></td></tr>");
			$('#tags').append("<tr><td>-</td></tr>");	

            var tags_sort = sort(tags);
            for (var id in tags_sort) {

                var row = "<tr>" +
                    "<td><a href='#' title='" + tags_sort[id][1] + "'>"+ tags_sort[id][0] + "</a></td>" +
                    "</tr>";

                $('#tags').append(row);

                if (id == 15) break;
            }
    });
}

function tags() {

    $('#main').html('');
}

function statistics() {

    $('#main').html('');
}

function sort(map) {

    var sortable = [];
    for (var key in map)
        if (map[key] != null)
            sortable.push([key, map[key]]);

    sortable.sort(function(b, a) {return a[1] - b[1]});

    return sortable;
}

list();