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
		    
			// message.innerText = 'There was an error injecting script : \n' + chrome.extension.lastError.message;

		});      	
		
	}, 10000); //every 10 sec
}

chrome.extension.onMessage.addListener(function(request, sender) {
	
	if (request.action == "getSource") {
		
		chrome.tabs.getSelected(null, function(tab) { // null defaults to current window
			
			var url = tab.url;

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