//	var host = "quiet-anchorage-6418.herokuapp.com";
var host = "localhost:9000";

var notificationTime = 2000;

function setDomains() {

    var url = "http://" + host + "/domains";

    var request = new XMLHttpRequest();
    request.open( "GET", url, false );
    request.send( null );

    var domains = request.responseText;
    localStorage.setItem("domains", domains);
}

function add_url(tab) {

    if (tab.status != "complete") return;

    var url = "http://" + host + "/add?url=" + tab.url;

    var request = new XMLHttpRequest();
    request.open( "GET", url, false );
    request.send( null );

    var response = request.responseText;

    if (response.length == 0) return; //TODO

    var webData = JSON.parse(response);

    console.log(tab.url.split("://")[1].replace("www.", ""));

    notification(tab["favIconUrl"], webData.title.replace(" - Wikipedia, the free encyclopedia", ""), ""); //TODO
}

function notification(favIcon, title, text) {

    var notification = window.webkitNotifications.createNotification(
        favIcon,    // The image.
        title,      // The title.
        text        // The body.
    );

    notification.show();

    setTimeout( function() { notification.cancel() }, notificationTime );
}