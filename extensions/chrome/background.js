// Conditionally initialize the options.
if (!localStorage.isInitialized) {
	localStorage.isActivated = true;   // The display activation.
    localStorage.frequency = 1;        // The display frequency, in minutes.
    localStorage.isInitialized = true; // The option initialization.
}

setInterval(function() {

    chrome.tabs.(null, {
        file: "javascripts/parse.js"

    }, function() {

        // message.innerText = 'There was an error injecting script : \n' + chrome.extension.lastError.message;

    });

}, 10000); //every 10 sec
