function build(title, words, url) {
	
	var words_count = Object.keys(words).length;
	
	var sortable = words_process(words);
	
	var tags_count = sortable.length;
	
	var domain = url.replace("http://", "").replace("https://", "").replace("www.", "");
	
	var title_array = (title + " " + domain).match(/[^\s"\d(){},'\-=_:;#%!<>&\|\*\?\[\]\.\/\+\\]{3,}/g);
	for (var i = 0; i < title_array.length; i++)
		title_array[i] = title_array[i].trim().toLowerCase();
	
	var mass = 0;
	for (var id in sortable)
		mass += sortable[id][1];
		
	domain = domain.split("/")[0];

	var head = "<p>" + title + "</p><hr><p>Tags</p>";
	var tags = "<div id='tags'><p>" + buildTags(sortable, title_array) + "</p></div>";
	var system_tags = "<div id='system_tags'><p>" + buildTags(sortable, title_array) + "</p></div>";
	var info = "<div id='info'><p>tags: " + tags_count + " words: " + words_count + " [" + (tags_count/words_count).toString().substring(0, 4) + "]</p></div>";
	var menu = "<div id='menu'>" +
					
				"<p><a href='#' class='add'>Add page</a></p>" +
//				"<p><a href='#' class='domain'>Add domain</a> | " + domain + "</p>" +
				"<p><a href='#' class='login'>Login</a></p></div>";

	
	return head + tags + "<hr>" + info + "<hr>" + menu;
}

function buildTags(sortable, title_array) {
	
	var out = [];
	
	for (var i = 0; i < sortable.length - 1; i++) {
				
		if (sortable[i][1] < 5) break;

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
			
			console.log(words);			
			
			message.innerHTML = build(tab.title, words, tab.url);
			
			markTags(message.getElementsByClassName('tag'));
			markTags(message.getElementsByClassName('pick'));			
			
			markMenu(message);			
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
			message.innerText = 'There was an error injecting script : \n' + chrome.extension.lastError.message;
    	}
  	});
}

window.onload = onWindowLoad();

function markTags(tags) {
	for (var i = 0; i < tags.length; i++) {	
		tags[i].addEventListener('click', click);
	}
}

function markMenu(message) {
	
	message.getElementsByClassName('add')[0].addEventListener('click', add);
	message.getElementsByClassName('login')[0].addEventListener('click', login);
	message.getElementsByClassName('domain')[0].addEventListener('click', domain);	
}

function domain() {
	
	console.log('domain');
	
    chrome.cookies.get({"url": 'http://quiet-anchorage-6418.herokuapp.com/', "name": 'session'}, function(cookie) {
		
		console.log(cookie);
    });
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
		
	chrome.tabs.getSelected(null, function(tab) {

		window.close();
		add_url(tab.url);
	});
}

function click() {
	
	if (this.getAttribute('class') === 'pick') this.setAttribute('class', 'tag');
	else this.setAttribute('class', 'pick');
}

function login() {
	
    var redirectUrl = "https://" + chrome.runtime.id + ".chromiumapp.org/";
    var clientId = "kiecajbpclgoigecdbpaodejolmbhhal";
    var authUrl = "http://quiet-anchorage-6418.herokuapp.com/?" +
        "client_id=" + clientId + "&" +
        "response_type=token&" +
        "redirect_uri=" + encodeURIComponent(redirectUrl);
 
    chrome.identity.launchWebAuthFlow({url: authUrl, interactive: true}, function(responseUrl) {
						   
    });
}
