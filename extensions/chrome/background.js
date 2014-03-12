function notification(tags_map) {
	
	chrome.tabs.getSelected(null, function(tab) {
		
		if (tags_map == null) return;
		
		var tags = [];
		for (var tag in tags_map) {
			tags.push(tags_map[tag][0]);
			if (tag > 10) break;
		}
					
		var title = tab.title; //.split(" - ")[0] // + ' [' + tags_map.length + ']'
			
		var heroku = "quiet-anchorage-6418.herokuapp.com";
		var localhost = "localhost:9000";
		
		var url = "http://" + heroku + "/add?url=" + tab.url + "&tags=" + JSON.stringify(tags) + "&title=" + tab.title;
	
		var xmlHttp = new XMLHttpRequest();
		xmlHttp.open("GET", url, false);
		xmlHttp.send(null);
		
		//chrome (autohide)
		chrome.notifications.create(
			
		    tab.url, {   
				type: 'basic', 
				iconUrl: tab.favIconUrl, 
				title: title, 
				message: tags.join(", ") 
		    }, function() {} );
						
		//webkit
		// var notification = window.webkitNotifications.createNotification(
		// 	tab.favIconUrl,                      // The image.
		// 	title, // The title.
		// 	tags.join(", ")      // The body.
		// );
		// 		
		// notification.show();				
	});
}

// Conditionally initialize the options.
if (!localStorage.isInitialized) {
	localStorage.isActivated = true;   // The display activation.
    localStorage.frequency = 1;        // The display frequency, in minutes.
    localStorage.isInitialized = true; // The option initialization.
}

// Test for notification support.
if (window.webkitNotifications) {
	
    setInterval(function() {
		
		chrome.tabs.executeScript(null, {
			file: "parse.js"
		
		}, function() {
    
			// If you try and inject into an extensions page or the webstore/NTP you'll get an error
	    	if (chrome.extension.lastError) {
				// message.innerText = 'There was an error injecting script : \n' + chrome.extension.lastError.message;
	    	}
	  	});      	
		
    }, 10000); //every 10 sec
}

chrome.extension.onMessage.addListener(function(request, sender) {
	
	if (request.action == "getSource") {
		
		chrome.tabs.getSelected(null, function(tab) { // null defaults to current window
			
			var tags = words_process(request.source['words']);
			
			if (tags.length == 0) return;
			
			var url = tab.url.split('://')[1].split('#')[0];

			var page = {};			
			page = JSON.parse(localStorage.getItem(url));
			
			if (page != null) {
				
				if (page['count'] == 0) {
					notification(tags);
					page['count']++;
					localStorage.setItem(url, JSON.stringify(page));
					console.log('notification: ' + url);
					return;	
				}
				
				if (page['scroll'] != request.source['scroll']) {
					page['scroll'] = request.source['scroll'];
					page['count']++;
					localStorage.setItem(url, JSON.stringify(page));					
					console.log('count: ' + page['count'] + ' scroll: ' + request.source['scroll'] + ' ' + url + '');
				}				
				
			} else {
				
				page = {};
				page['tags'] = tags;
				page['count'] = 0;
				page['scroll'] = request.source['scroll'];
				localStorage.setItem(url, JSON.stringify(page));
				console.log('created: ' + url);
			}
		});
	}
});