var states = {0:"denied", 1:"unchecked", 2:"manual", 3:"trusted"};

function query(text) {

    $.get("/query", {text: text}, function( data ) {});
}

function domains() {

    $.get("/alldomains", {},

        function(domains) {

            var tags = {};

            var main = $('#main');

            main.html('');

            main.append('<div id="out"><table id="data"></table></div>');

            for (var id in domains) {

                var domains_tags = JSON.parse(domains[id]['tags']);

                for (var tag in domains_tags) {

                    if (tags[tag] != null) tags[tag] += parseInt(domains_tags[tag]);
                    else tags[tag] = parseInt(domains_tags[tag]);
                }

                var favIcon = "";

                if (domains[id]['favIconFormat']) favIcon = "<a><img src='"+  "/assets/favicons/" + domains[id]['url'] + "." + domains[id]['favIconFormat'] +  "' height='16' width='16'></a>";

                var row =

                    "<tr>" +

                    "<td>" + favIcon + "</td>" +

                    "<td class='study'><a href='" + "http://" + domains[id]['url'] + "' target='_blank'>" + domains[id]['url'] + "</a></td>" +

                    "<td>" + domains[id]['title'] + "</td>" +

                    "<td>" + states[domains[id]['state']] + "</td>" +

                    "</tr>";

                $('#data').append(row);
            }

            main.append('<table id="tags" border="0"></table>');

            var tags_sort = sort(tags);

            for (var id in tags_sort) {

                var row = "<tr>" +
                    "<td><a href='javascript:sort()' title='" + tags_sort[id][1] + "'>"+ tags_sort[id][0] + "</a></td>" +
                    "</tr>";

                $('#tags').append(row);

                if (id == 15) break;
            }


        }
    );
}

function pages() {

    $.get("/pages", {},
	
        function(data) {
			
			//data

            var main = $('#main');

            main.html('');
            main.append('<div id="out"><table id="data"></table></div>');
			
			var domains = {};

            var favIconsFormat = {};

            var tags = {};

            for (var id in data) {
				
				var domain = data[id]['url'].replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];

				if (domains[domain] != null) domains[domain]++; //TODO
				else {

                    if (data[id]['favIconFormat']) favIconsFormat[domain] = data[id]['favIconFormat'];
                    domains[domain] = 1;
                }

                var url_tags = JSON.parse(data[id]['tags']);

                for (var tag in url_tags) {

                    if (tags[tag] != null) tags[tag] += parseInt(url_tags[tag]);
                    else tags[tag] = parseInt(url_tags[tag]);                    
                }
				
				var title = data[id]['title'].replace("\<", "\<\\");
				
				if (title.length > 100) title = title.substring(0, 110) + "...";
				
				var progress_block = "&#8211;";
				// var progress_block = "&#183;"; //midl dot ··················

                var favIcon = "";

                if (data[id]['favIconFormat']) favIcon = "<a><img src='" + "/assets/favicons/" + domain + "." + data[id]['favIconFormat'] + "' height='16' width='16'></a>";

                var row = "<tr>" +

                   	// "<td><font color='gray'>9:24 pm</font></td>" +

                    "<td>" + favIcon + "</td>" +
                    
					"<td class='study'>" +
					"<a href='" + data[id]['url'] + "' target='_blank'>" + title + "</a>" +
					
					// "<a><font color='green'>" + Object.keys(JSON.parse(data[id]['usertags'])).length + "</font></a>" + 
					// "<a><font color='gray'>" + Object.keys(JSON.parse(data[id]['tags'])).length + "</font></a>" +
					
//					"<span title='" + Object.keys(JSON.parse(data[id]['tags'])).length + "'><font color='#00ffff'>" +
					 
//					Array(Math.floor(Object.keys(JSON.parse(data[id]['usertags'])).length/5 + 4)).join(progress_block) + "</font>" +
					
					// Math.floor(
//					"<font color='gray'>" + Array(Math.floor(Object.keys(JSON.parse(data[id]['tags'])).length/20)).join(progress_block) + "</font></span>" +
					
					
					
					// Object.keys(JSON.parse(data[id]['usertags'])).join(", ") +
					"</td>" +
					
                    // "<td>" +  "</td>" +
                    // "<td>" + Object.keys(JSON.parse(data[id]['tags'])).slice(0, 15).join(", ") + ", ...</td>" + 
					
					"</tr>";

                $('#data').append(row);
            }	        
			
//			$('#data').append("<tr></tr><tr></tr><tr><td></td><td><a href='#'>Load more</a></td></tr><tr></tr><tr></tr>");
			
			//domains

            main.append('<table id="domains" border="0"></table>');

            var domains_sort = sort(domains);

            for (var id in domains_sort) {

                var favIcon = "";

                if (favIconsFormat[domains_sort[id][0]]) {

                    var row = "<tr>" +
                        "<td><a href='"+ "http://" + domains_sort[id][0] + "' target='_blank'><img src='"+  "/assets/favicons/" + domains_sort[id][0] + "." + favIconsFormat[domains_sort[id][0]] + "' height='16' width='16' title='" + domains_sort[id][0] + "'></a></td>" +
                        "</tr>";

                    $('#domains').append(row);
                }

                if (id == 15) break;
            }

            //tags

            main.append('<table id="tags" border="0"></table>');

            var tags_sort = sort(tags);
            for (var id in tags_sort) {

                var row = "<tr>" +
                    "<td><a href='javascript:sort()' title='" + tags_sort[id][1] + "'>"+ tags_sort[id][0] + "</a></td>" +
                    "</tr>";

                $('#tags').append(row);

                if (id == 15) break;
            }

//            $('#main').append('<div id="save"><td><a href="javascript:save()">save</a></td></div>');
    });


}

function achievements() {

    $('#main').html('');
}

function sort(map) {

    var sortable = [];
    for (var key in map)
        if (map[key] != null)
            sortable.push([key, map[key]]);

    sortable.sort(function(b, a) {return a[1] - b[1]});

    return sortable
}