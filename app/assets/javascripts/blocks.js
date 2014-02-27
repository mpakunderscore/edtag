var api_url = 'http://en.wikipedia.org/w/api.php?';

function lambda() {
	
	var url = api_url + 'action=query&list=random' +
	'&rnlimit=5&callback=?&format=json';
	
	$.getJSON(url ,function(data) {

		var title = data.query.random[0].title;
		console.log(title);
		
		
		// var exclude = '(Talk:)|(User:)|(User talk:)|(Template:)|(Category:)|(File:)|(Wikipedia:)';
		var exclude = '(Template:)|(Category:)|(File:)|(Wikipedia:)';
		if (title.match('^' + exclude)) lambda();
		// if (title.startWith("Talk:")) random();
		else {
			say(title);
			get_data(data.query.random[0].title);				
		}
	});
}

function process_old() {

	var phrase = 'words, url, text, speach, question, command';

	var page = $("#search").val();
	
	if (page === '') page = 'Everything';	
	else {
		$("#search").val('');
		// say(phrase);
	}
	
	// get_data(page);
}

function get_data(page) {
	
	var url = api_url + 'action=query&prop=extracts&titles=' + page
	+ '&rvprop=content&callback=?&format=json';
	
	$.getJSON(url ,function(data) {

		console.log(data);
		var content = data.query.pages[Object.keys(data.query.pages)[0]].extract;
		
		$("#main").html('');	
		if (content != null && content.length > 0) build_page(page, content);
				
	});
}

function build_page(page, content) {
	
	// var img = $('<img class="img" src="heart.gif">');
	
	var video = $('<div><img src=""/></div>');
	video.attr('class', 'block video');		

	var search = $('<div><img src=""/></div>');
	search.attr('class', 'block search');			

	var block = $('<div></div>');
	block.attr('class', 'block');
	var wiki = $('<div></div>');
	wiki.attr('class', 'wiki');
	
	
	var info = $('<div>' + get_info(page, content) + '</div>');
	info.attr('class', 'info');				
	
	block.append(info);	
	
	wiki.append('<p>wiki</p>');
	wiki.append(content);
	block.append(wiki);	
	
	// $("#main").append(video);	   	
	// $("#main").append(search);	   		
	$("#main").append(block);   
}

function say(phrase) {
	$("#search").attr('placeholder', phrase);
}

//$("#main").append('<center><a href="" style="text-decoration: none;" onclick="destroy()">∞</a></center>');
//$("#main").html('');
function destroy() {
	document.body.innerHTML = '';
}

function get_info(page, content) {

	var text = content;	
	
	var regex = /([^\s"\d(){},'\-=_:;#%!<>&\?\[\]\.\/\+\\]{6,})/g;
	var words_array = text.match(regex);
	var words_map = {"": 0};
	
	for (var i = 0; i < words_array.length; i++) {
		
		var word = words_array[i].toLowerCase();
		
		if (word in words_map) words_map[word] = words_map[word] + 1;
		else words_map[word] = 1;
	}
	
	words_map[page.toLowerCase()] = 0;
	
	var out = '<table>';
	
	var sortable = [];
	for (var word in words_map) sortable.push([word, words_map[word]])		  
	sortable.sort(function(b, a) {return a[1] - b[1]})		  
	
	
	for (var i = 0; i < Math.min(sortable.length, 20); i++) {
		
		if (sortable[i][1] < 4) break;
		
		var link = "<a href='#' onclick='get_data(\"" + sortable[i][0] + "\")'>"
		+ sortable[i][0]
		+ "</a>";
		
		out += '<tr>';
		
		out += '<td align="right">' + link + '</td>';
		out += '<td>' + "&sdot;".repeat(Math.floor(sortable[i][1]/3)) + '</td>';				
				
		out += '</tr>';
	}
	
	out += '</table>';
	return out;
}

String.prototype.repeat = function(num) {
	
    return new Array(num + 1).join(this);
}