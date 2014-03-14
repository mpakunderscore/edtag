function build(title, words) {
	
	var words_count = Object.keys(words).length;
	
	var sortable = words_process(words);
	
	var tags_count = sortable.length;
	
	var title_array = title.match(/[^\s"\d(){},'\-=_:;#%!<>&\|\*\?\[\]\.\/\+\\]{3,}/g);
	for (var i = 0; i < title_array.length; i++)
		title_array[i] = title_array[i].trim().toLowerCase();
	
	var mass = 0;
	for (var id in sortable) 
		mass += sortable[id][1];
	
	var set = 0;
	for (var i = 0; i < sortable.length - 1; i++) {
		
		if (set > 0.2) sortable.splice(i, i + 1);
		else set += sortable[i][1]/mass;
	}

	var head = "<p>" + title + "</p><hr><p>Tags</p>";
	var tags = "<div id='tags'><p>" + buildTags(sortable, title_array) + "</p></div>";
	var system_tags = "<div id='system_tags'><p>" + buildTags(sortable, title_array) + "</p></div>";
	var info = "<div id='info'><p>tags: " + tags_count + " words: " + words_count + " [" + (tags_count/words_count).toString().substring(0, 4) + "]</p></div>";
	var menu = "<div id='menu'>" +
					
				"<p><a href='#' class='add'>Add</a></p>" +
				"<p><a href='#' class='analysis'>Analysis</a></p>" +			
				"<p><a href='#' class='search'>Search like this one</a></p></div>";
	
	//<a href='#' id='add'>retain</a>
	// var menu = "<div id='menu'><button id='add'>add</button></div>";	
	
	return head + tags + "<hr>" + info + "<hr>" + menu;
}

function buildTags(sortable, title_array) {
	
	var out = [];
	
	for (var i = 0; i < sortable.length - 1; i++) {
				
		if (sortable[i][1] < 2) break;

		var type = 'tag';
		if (title_array.indexOf(sortable[i][0]) > -1) type = 'pick';

		out[i] = "<a class='" + type + "' href='#' title='" + sortable[i][1] + "'>" + sortable[i][0] + "</a>";
	}
	
	var line = out.join(". ");
	return line;
}

chrome.extension.onMessage.addListener(function(request, sender) {
	
	if (request.action == "getSource") {
		
		chrome.tabs.getSelected(null, function(tab) { // null defaults to current window
						
			if (message.innerHTML.length > 10) return; //TODO dont reload popup from background message
			
			var words = request.source['words'];
			
			message.innerHTML = build(tab.title, words);
			
			markTags(message.getElementsByClassName('tag'));
			markTags(message.getElementsByClassName('pick'));			
			
			markSearch(message);
			markAdd(message);			
		});
	}
});

function onWindowLoad() {

	var message = document.querySelector('#message');

	chrome.tabs.executeScript(null, {
		file: "parse.js"
		
	}, function() {
    
		// If you try and inject into an extensions page or the webstore/NTP you'll get an error
    	if (chrome.extension.lastError) {
			// message.innerText = 'There was an error injecting script : \n' + chrome.extension.lastError.message;
    	}
  	});
}

window.onload = onWindowLoad();

function markTags(tags) {
	for (var i = 0; i < tags.length; i++) {	
		tags[i].addEventListener('click', click);
	}
}

function markSearch(message) {
	var search = message.getElementsByClassName('search')[0];
	search.addEventListener('click', google);
}

function markAdd(message) {
	var search = message.getElementsByClassName('add')[0];
	search.addEventListener('click', add);
}

function google() {

	var tags = [];
	var nodes = chrome.extension.getViews({type: "popup"})[0].document.getElementsByClassName('pick');
	for (var i = 0; i < nodes.length; i++) 
		tags.push(nodes[i].text);

	chrome.tabs.create({url: "http://www.google.com/search?q=" + tags.join('+')});
}

function add() {
	
	var tags = {};	
	var nodes = chrome.extension.getViews({type: "popup"})[0].document.getElementsByClassName('tag');
	for (var i = 0; i < nodes.length; i++) 
		tags[nodes[i].text] = nodes[i].title;
		
	var usertags = {};	
	nodes = chrome.extension.getViews({type: "popup"})[0].document.getElementsByClassName('pick');
	for (var i = 0; i < nodes.length; i++) 
		usertags[nodes[i].text] = nodes[i].title;
		
	chrome.tabs.query({currentWindow: true, active: true}, function(tabs) {
		
		var tab = tabs[0];
		
		add_url(tab.url.split('#')[0], tags, tab.title, tab.favIconUrl, usertags);	
		
		window.close();	
	});
}

function click() {
	
	if (this.getAttribute('class') === 'pick') this.setAttribute('class', 'tag');
	else this.setAttribute('class', 'pick');
}


// var notification = window.webkitNotifications.createNotification(
// 	'http://i.stack.imgur.com/Mmww2.png',                      // The image.
// 	'Achievements: javascript beginner', // The title.
// 	'JavaScript is almost entirely object-based. JavaScript objects are associative arrays, augmented with prototypes.'      // The body.
// );
// 		
// notification.show();

// chrome.notifications.create(
// 
//     null, {   
// 		type: 'basic', 
// 		iconUrl: 'http://i.stack.imgur.com/Mmww2.png', 
// 		title: 'Achievements: javascript beginner', 
// 		message: 'JavaScript is almost entirely object-based. JavaScript objects are associative arrays, augmented with prototypes.' 
//     }, function() {} );