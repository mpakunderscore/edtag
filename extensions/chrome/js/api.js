//var host = "https://edtag.io";
var host = "http://localhost:9000";

var notificationTime = 10000;

var notID = 0;

function setDomains() {

    var url = host + "/domains";

    var request = new XMLHttpRequest();
    request.open( "GET", url, false );
    request.send( null );

    var domains = request.responseText;
    localStorage.setItem("domains", domains);
}

function add_url(tab) {

    if (tab.status != "complete") return;

    var url = host + "/add?url=" + encodeURIComponent(tab.url);

    var request = new XMLHttpRequest();
    request.open( "GET", url, false );
    request.send( null );

    var response = request.responseText;

    if (response.length == 0) return; //TODO

    var webData = JSON.parse(response);

    var url = tab.url.split("://")[1].replace("www.", "");

    console.log(url);

    var iconUrl = "https://s3.amazonaws.com/edtag/";

    webData.favIconFormat = "ico";

    if (webData.favIconFormat) iconUrl += url.split("/")[0] + "." + webData.favIconFormat;

    else iconUrl += "blank.ico";

    notification(iconUrl, webData.title.replace(" - Wikipedia, the free encyclopedia", ""), webData.tags); //TODO
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

    chrome.notifications.create("id"+notID++, options, creationCallback);
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

function sort(tags) {

    var sortable = [];
    var sorted_tags = [];

    for (var tag in tags)
        if (tags[tag] != null)
            sortable.push([tag, tags[tag]]);

    sortable.sort(function(b, a) {return a[1] - b[1]});

    for(var key in sortable) {
        sorted_tags.push(sortable[key][0]);
    }

    return sorted_tags;
}