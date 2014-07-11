var host = "https://edtag.io";
//var host = "http://localhost:9000";

var notificationTime = 3000;

var notID = 0;

function add_url(tab, userId) {

    if (tab.status != "complete") return;

    var url = host + "/add?url=" + encodeURIComponent(tab.url) + "&userId=" + userId;

    var request = new XMLHttpRequest();
    request.open( "GET", url, false );
    request.send( null );

    var response = request.responseText;

    if (response.length == 0) return; //TODO

    var link = JSON.parse(response);

    var tabUrl = tab.url.split("://")[1].replace("www.", "");

//    console.log(tabUrl);

    var iconUrl = "https://s3.amazonaws.com/edtag/";

    link.favIconFormat = "ico";

    if (link.favIconFormat) iconUrl += tabUrl.split("/")[0] + "." + link.favIconFormat;

    else iconUrl += "blank.ico";

    var tags = link.tags;

    var tagNames = [];
    for (var id in tags)
        tagNames.push(tags[id].name);

    notification(iconUrl, link.title.replace(" - Wikipedia, the free encyclopedia", ""), tagNames.join(", ")); //TODO
}

function notification(iconUrl, title, text) {

    var options = {
        type : "basic",
        title: title,
        message: text,
        expandedMessage: "Longer part of the message"
    };

    options.iconUrl = iconUrl;

    options.priority = 0;

    options.buttons = [];
//    options.buttons.push({ title: "ok" });

    var id = "id" + notID++;

    chrome.notifications.create(id, options, creationCallback);
}

function creationCallback(notID) {

    console.log("Succesfully created " + notID + " notification");

//    if (document.getElementById("clear").checked) {

        setTimeout(function() {

            chrome.notifications.clear(notID, function(wasCleared) {
                console.log("Notification " + notID + " cleared: " + wasCleared);
            });

        }, notificationTime);
//    }
}
