function notification(tags_map) {
	
	chrome.tabs.getSelected(null, function(tab) {
		
		if (tags_map == null) return;
		
		var tags = {};
		for (var id in tags_map) {
			tags[tags_map[id][0]] = tags_map[id][1];
			if (id > 10) break;
		}
								
		add_url(tab.url, tags, tab.title, tab.favIconUrl);	//TODO > transport.js	
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
	
		//     setInterval(function() {
		// 
		// chrome.tabs.executeScript(null, {
		// 	file: "parse.js"
		// 
		// }, function() {
		//     
		// 	// If you try and inject into an extensions page or the webstore/NTP you'll get an error
		// 	    	if (chrome.extension.lastError) {
		// 		// message.innerText = 'There was an error injecting script : \n' + chrome.extension.lastError.message;
		// 	    	}
		// 	  	});      	
		// 
		//     }, 10000); //every 10 sec
}

chrome.extension.onMessage.addListener(function(request, sender) {
	
	if (request.action == "getSource") {
		
		chrome.tabs.getSelected(null, function(tab) { // null defaults to current window
			
			var tags = words_process(request.source['words']);
			
			if (tags.length == 0) return;
			
			var url = tab.url.split('#')[0]; //.split('://')[1];

			var page = {};			
			page = JSON.parse(localStorage.getItem(url));
			
			if (page != null) {
				
				if (page['scroll'] != request.source['scroll']) { //TODO pages without scroll
					
					page['scroll'] = request.source['scroll'];
					
					if (page['count'] == 0) {
						notification(tags);
					}
					
					page['count']++;
					localStorage.setItem(url, JSON.stringify(page));
					console.log('count: ' + page['count'] + ' ' + url);
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