$('#search').keypress(function(event) {
    if (event.which == 13) {
        process2();
    }
});


function process2() {

    var entered = $("#search").val();

    if (entered === '') return;

    query(entered);

    $('#search').val('');

    $('#answer').html("<p>" + entered + "</p>");
}

function query(text) {

    $.get("/query", {text: text}, function( data ) {});
}

function list() {
    $.get(
        "/get",
        {},
        function(data) {

            $('#main').html('')
            $('#main').append('<table id="data" border="0"></table>');

            for (var id in data) {
                $('#data').append("<tr><td>" + data[id]['url'].replace("http://", "").replace("https://", "").split("/")[0] + "</td>" +
                                  "<td><img src="+ data[id]['faviconurl'] +" height='19' width='19'></td>" +
                                  "<td><a href=" + data[id]['url'] + ">" + data[id]['title'] + "</a></td>" +
                                  "<td>" + Object.keys(JSON.parse(data[id]['usertags'])).join(", ") + "</td>" +
                                  "<td>" + Object.keys(JSON.parse(data[id]['tags'])).join(", ") + "</td></tr>");
            }
        }
    );
}

function tags() {

    $('#main').html('');
}

list();