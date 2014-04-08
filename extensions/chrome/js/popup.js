function add() {

    chrome.tabs.query({ currentWindow: true, active: true }, function (tabs) {

        add_url(tabs[0]);
    });
}

function login() { //TODO

    chrome.identity.getAuthToken({ 'interactive': true }, function(token) {
        // Use the token.
    });
}

add();

setDomains();