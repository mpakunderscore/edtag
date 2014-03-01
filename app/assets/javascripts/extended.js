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