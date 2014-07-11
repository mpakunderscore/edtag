var host = "https://edtag.io";
//var host = "http://localhost:9000";

//setDomains();

//var delay = 10000;
//
//setInterval(function() {
//
//    var domains = JSON.parse(localStorage.getItem("domains"));
//    chrome.tabs.query({ currentWindow: true, active: true }, function (tabs) {
//
//        var domain = tabs[0].url.split("://")[1].split("/")[0].replace("www.", "");
//
//        if (domains.indexOf(domain) >= 0) {
//            add_url(tabs[0]);
//        }
//    });
//
//}, delay);
//every 10 sec

chrome.browserAction.onClicked.addListener(function(tab) {

    var userId = localStorage["userId"];
    if (!userId) {

        var url = host + "/api/user";

        var request = new XMLHttpRequest();
        request.open("GET", url, false);
        request.send(null);

        var response = request.responseText;

        if (response.length == 0) {

            var newURL = host + "/login";
            chrome.tabs.create({ url: newURL });
            return;
        }

        var user = JSON.parse(response);

        userId = user.id;
    }

    add_url(tab, userId);
});