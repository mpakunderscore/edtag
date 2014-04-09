setDomains();

var delay = 10000;

setInterval(function() {

    var domains = JSON.parse(localStorage.getItem("domains"));
    chrome.tabs.query({ currentWindow: true, active: true }, function (tabs) {

        var domain = tabs[0].url.split("://")[1].split("/")[0].replace("www.", "");

        if (domains.indexOf(domain) >= 0) {
            add_url(tabs[0]);
        }
    });

}, delay); //every 10 sec

chrome.browserAction.onClicked.addListener(function(tab) {
    add_url(tab);
});