function add_url(tab_url) { //TODO move out
	
//	var heroku = "quiet-anchorage-6418.herokuapp.com";
	var localhost = "localhost:9000";
	
//	var url = "http://" + heroku + "/add";
    var url = "http://" + localhost + "/add?url=" + tab_url;

    var request = new XMLHttpRequest();
    request.open( "GET", url, false );
    request.send( null );

    return request.responseText;
}

function notification(title, favIconUrl, domain, usertags) {
	
	//chrome (autohide)
	// chrome.notifications.create(
	// 	
	//     tab_url, {   
	// 		type: 'basic', 
	// 		iconUrl: favIconUrl, 
	// 		title: title, 
	// 		message: '[' + domen + ']' + '[' + Object.keys(usertags).join(", ") + ']' +  Object.keys(tags).join(", ") 
	//     }, function() {} );
					
	//webkit
	var notification = window.webkitNotifications.createNotification(
		favIconUrl,                      // The image.
		title, // The title.
		domain + ' | ' + Object.keys(usertags).join(", ")      // The body.
	);
			
	notification.show();	
}