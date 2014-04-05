function add_url(tab_url, tags, title, favIconUrl, usertags) { //TODO move out
	
//	var heroku = "quiet-anchorage-6418.herokuapp.com";
	var localhost = "localhost:9000";
	
//	var url = "http://" + heroku + "/add";
    var url = "http://" + localhost + "/add";

	var xmlHttp = new XMLHttpRequest();
	xmlHttp.open("POST", url, false);
	xmlHttp.setRequestHeader('Content-Type', 'application/json');
	xmlHttp.send(JSON.stringify({url: tab_url, tags: JSON.stringify(usertags), title: title, faviconurl: favIconUrl}));

	var domain = tab_url.replace("http://", "").replace("https://", "").replace("www://", "").split("/")[0];	
	notification(title, favIconUrl, domain, usertags);
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