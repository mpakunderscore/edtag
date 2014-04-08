setDomains();

setInterval(function() {

    var domains = JSON.parse(localStorage.getItem("domains"));
    chrome.tabs.query({ currentWindow: true, active: true }, function (tabs) {

        var domain = tabs[0].url.split("://")[1].split("/")[0].replace("www.", "");

        if (domains.indexOf(domain) >= 0) {
            add_url(tabs[0]);
        }
    });

}, 10000); //every 10 sec
