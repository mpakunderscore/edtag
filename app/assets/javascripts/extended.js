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

            console.log(data);

            for (var id in data) {
                $('#main').append("<p><a href=" + data[id]['url'] + "\">" + data[id]['title'] + "</a> " + data[id]['tags'] + "</p>");
            }
        }
    );
}

list();