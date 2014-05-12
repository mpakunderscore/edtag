var states = {0:"-", 1:"?", 2:"~", 3:"+"};

var colors = {0:"red", 1:"gray", 2:"blue", 3:"green"};

var pages_list = [];

var selected_tags = [];

function query(text) {

    $.get("/query", {text: text}, function( data ) {});
}

function domains() {

    $("#menu_domains").css("border-bottom", "1px solid #aeaeae"); //TODO
    $("#menu_pages").css("border-bottom", "0");

    $.get("/alldomains", {},

        function(domains) {

            if (domains.length == 0) {

                $('#main').html('<div id="out"><p style="padding-left: 20px">Domains table is empty.</p></div>');
                return;
            }

            var tags = {};

            var main = $('#main');

            main.html('');

            main.append('<div id="out"><table id="data"></table></div>');

            for (var id in domains) {

                var domains_tags = JSON.parse(domains[id]['tags']);

                for (var tag in domains_tags) {

                    if (tags[tag] != null) tags[tag] += parseInt(domains_tags[tag]);
                    else tags[tag] = parseInt(domains_tags[tag]);

                }

                var favIcon = "";

                if (domains[id]['favIconFormat']) favIcon = "<a><img src='"+  "https://s3.amazonaws.com/edtag/" + domains[id]['url'] + "." + domains[id]['favIconFormat'] +  "' height='16' width='16'></a>";

                var row =

                    "<tr style='width: 100%;'>" +

                    "<td>" + favIcon + "</td>" +

                    "<td class='study'><a href='" + "http://" + domains[id]['url'] + "' target='_blank' title='" + sort(domains_tags).join("  ") + "'>" + domains[id]['url'] + "</a></td>" +

                    "<td>" + (domains[id]['state'] == 3 ? "<font color='green'>&#8226;</font>" : "") +  "</td>" +

                    "<td>" + domains[id]['title'] + "</td>" +

                    "</tr>";

                $('#data').append(row);
            }

            main.append('<table id="tags" border="0"></table>');

            var tags_sort = sort(tags);

            for (var id in tags_sort) {

                var name = tags_sort[id][0];

                if (name.length > 13) name = name.substring(0, 13) + "..";

                var row = "<tr>" +
                    "<td><a href='javascript:void(0)' onclick='tag_sort(this)' title='" + tags_sort[id][1] + "'>"+ name + "</a></td>" +
                    "</tr>";

                $('#tags').append(row);

                if (id == 15) break;
            }


        }
    );
}

function pages() {

    $("#menu_pages").css("border-bottom", "1px solid #aeaeae");
    $("#menu_domains").css("border-bottom", "0");

    if (pages_list.length == 0)

        jQuery.ajax({
            url: "/pages",
            success: function(pages) {
                pages_list = pages.slice();
            },
            async: false
        });

    var main = $('#main');

    if (pages_list.length == 0) {

        main.html('<div id="out"><p style="padding-left: 20px">Pages table is empty.</p></div>');
        return;
    }

    main.html('');
    main.append('<div id="out"><table id="data"></table></div>');

	var domains = {};

    var favIconsFormat = {};

    var tags = {};

    for (var id in pages_list) {

        var url_tags = JSON.parse(pages_list[id]['tags']);

        var remove = false;
        for (var tid in selected_tags) {
            if (!(selected_tags[tid] in url_tags)) {
                remove = true;
            }
        }

        if (remove) continue;

        var domain = pages_list[id]['url'].replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];

        if (domains[domain] != null) domains[domain]++; //TODO
        else {

            if (pages_list[id]['favIconFormat']) favIconsFormat[domain] = pages_list[id]['favIconFormat'];
            domains[domain] = 1;
        }

        for (var tag in url_tags) {

            if (tags[tag] != null) tags[tag] += parseInt(url_tags[tag]);
            else tags[tag] = parseInt(url_tags[tag]);
        }

        var title = pages_list[id]['title'].replace("\<", "\<\\");

        if (title.length > 100) title = title.substring(0, 110) + "...";

        var favIcon = "";

        if (pages_list[id]['favIconFormat']) favIcon = "<a><img src='" + "https://s3.amazonaws.com/edtag/" + domain + "." + pages_list[id]['favIconFormat'] + "' height='16' width='16'></a>";

        var row = "<tr>" +

        "<td>" + favIcon + "</td>" +

        "<td class='study'>" + "<a href='" + pages_list[id]['url'] + "' target='_blank' title='" + sort(url_tags).join("  ") + "'>" + title + "</a>" + "</td>" + "</tr>";

        $('#data').append(row);

    }

//    main.append('<table id="domains" border="0"></table>');
//
//    var domains_sort = sort(domains);
//
//    for (var id in domains_sort) {
//
//        if (favIconsFormat[domains_sort[id][0]]) {
//
//            var row = "<tr class='domain_control'>" +
//                "<td><a href='"+ "http://" + domains_sort[id][0] + "' target='_blank'><img src='"+  "https://s3.amazonaws.com/edtag/" + domains_sort[id][0] + "." + favIconsFormat[domains_sort[id][0]] + "' height='16' width='16' title='" + domains_sort[id][0] + "'></a><span></td>" +
//                "</tr>";
//
//            $('#domains').append(row);
//        }
//
//        if (id == 15) break;
//    }

    //tags

    main.append('<table id="tags" border="0"></table>');

    for (var tid in selected_tags) {

        var row = "<tr>" + "<td><a href='javascript:void(0)' onclick='tag_sort(this)' title='remove' style='color: rgb(254, 65, 50);'>"+ selected_tags[tid] + "</a></td>" + "</tr>";
        $('#tags').append(row);
    }

    var tags_sort = sort(tags);
    for (var id in tags_sort) {

        var name = tags_sort[id][0];

        if (selected_tags.indexOf(name) >= 0) continue;

        if (name.length > 25) name = name.substring(0, 21) + "..";

        var row = "<tr>" + "<td><a href='javascript:void(0)' onclick='tag_sort(this)' title='" + tags_sort[id][1] + "'>"+ name + "</a></td>" + "</tr>";

        $('#tags').append(row);

        if (id >= 15 - selected_tags.length) break;
    }

//            $('#main').append('<div id="save"><td><a href="javascript:save()">save</a></td></div>');
}

function sort(map) {

    var sortable = [];
    for (var key in map)
        if (map[key] != null)
            sortable.push([key, map[key]]);

    sortable.sort(function(b, a) {return a[1] - b[1]});

    return sortable
}

function tag_sort(clicked) {

    if (selected_tags.indexOf(clicked.text) < 0)
        selected_tags.push(clicked.text);

    else selected_tags.splice(selected_tags.indexOf(clicked.text), 1);

    pages();
}

pages();