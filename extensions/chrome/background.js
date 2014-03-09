function notification() {
	
    var time = /(..)(:..)/.exec(new Date());     // The prettyprinted time.
    var hour = time[1] % 12 || 12;               // The prettyprinted hour.
    var period = time[1] < 12 ? 'a.m.' : 'p.m.'; // The period of the day.
	
	chrome.tabs.getSelected(null, function(tab) {

	    var notification = window.webkitNotifications.createNotification(
	    	tab.favIconUrl,                      // The image.
	      	tab.title.split(" - ")[0], // The title.
	      	'webrequest, chrome, extension, stack, api, user, enthusiast, privacygs, notifications, apps, tool, snippet, launches'      // The body.
	    );
		
	    notification.show();		
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
	
    // While activated, show notifications at the display frequency.
    if (JSON.parse(localStorage.isActivated)) { notification(); }

    var interval = 0; // The display interval, in minutes.

    setInterval(function() {
      	interval++;

      	if (
        	JSON.parse(localStorage.isActivated) &&
          	localStorage.frequency <= interval
      	) {
        	notification();
        	interval = 0;
      	}
    }, 60000);
}