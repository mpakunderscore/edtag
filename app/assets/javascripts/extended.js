var pages_list = [];

var selected_tags = [];

var domains_selected_tags = [];

function query(text) {

    $.get("/query", {text: text}, function( data ) {});
}

function sources() {

    $.get("/alldomains", {},

        function(domains) {

            var main = $('#main');

            if (domains.length == 0) {

                main.html('<ul id="out"><li>Domains list is empty.</li></ul>');
                return;
            }

            var tags = {};

            main.html('');

            main.append('<ul id="out"></ul>');

            for (var id in domains) {

                var domains_tags = JSON.parse(domains[id]['tags']);

                for (var tag in domains_tags) {

                    if (tags[tag] != null) tags[tag] += parseInt(domains_tags[tag]);
                    else tags[tag] = parseInt(domains_tags[tag]);

                }

                var favIcon = "",
                    link = "<a href='" + "http://" + domains[id]['url'] + "' target='_blank' title='" + sort(domains_tags).join(" . ").replace(/,/g, ": ") + "'>" + domains[id]['url'] + "</a>",
                    title = "<span class='title'>"+domains[id]['title']+"</span>",
                    trusted = (domains[id]['state'] == 3) ? "<sup title='Trusted domain'>&#9679;</sup>" : " ";

                if (domains[id]['favIconFormat']) favIcon = "<a><img src='"+  "https://s3.amazonaws.com/edtag/" + domains[id]['url'] + "." + domains[id]['favIconFormat'] +  "' height='16' width='16'></a>";

                var item = "<li class='domain'>" +favIcon + link + title + "</li>";

                $('#out').append(item);
            }

            main.append('<nav id="tags" class="menu"></nav>');

            for (var tid in domains_selected_tags) {

                var row = "<a href='javascript:void(0)' onclick='domains_tag_sort(this)' title='remove' class='selected'>"+ domains_selected_tags[tid] + "</a>";
                $('#tags').append(row);
            }

            var tags_sort = sort(tags);
            for (var id in tags_sort) {

                var name = tags_sort[id][0];

                if (domains_selected_tags.indexOf(name) >= 0) continue;

                if (name.length > 25) name = name.substring(0, 21) + "..";

                row = "<a href='javascript:void(0)' onclick='domains_tag_sort(this)' title='" + tags_sort[id][1] + "'>"+ name + "</a>";

                $('#tags').append(row);

                if (id >= 15 - domains_selected_tags.length) break;
            }


        }
    );
}

function courses() {

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

        main.html('<ul id="out"><li>Pages list is empty.</li></ul>');
        return;
    }

    main.html('');
    main.append('<ul id="out"></ul>');

	var domains = {},
      favIconsFormat = {},
      tags = {},
      favIcon = "";

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

        title = (title.length > 100) ? title.substring(0, 100) + "..." : title;

        var volume_block = "&#8211;",
            favIconURL = (pages_list[id]['favIconFormat']) ? "https://s3.amazonaws.com/edtag/" + domain + "." + pages_list[id]['favIconFormat'] : '',
            favIcon = "<img src=" + favIconURL + ">",
            linkTags = "id: " + pages_list[id]['id'] + "\n\n" + sort(url_tags).join("  ").replace(/,/g, ":") + "\n\nWords count: " + pages_list[id]['wordsCount'] + "\nTags count: " + pages_list[id]['uniqueWordsCount'],
            link = "<a href='" + pages_list[id]['url'] + "' target='_blank' title='" + linkTags + "'>" + title + "</a>",
            wordsCount = "<span class='volume'>" + Array(Math.floor(pages_list[id]['uniqueWordsCount']/100)).join(volume_block) + "<span>",
            item = "<li class='page'>" + favIcon + link + wordsCount + "</li>";

        $('#out').append(item);

    }

    main.append('<nav id="tags" class="menu"></nav>');

    for (var tid in selected_tags) {

        var row = "<a href='javascript:void(0)' onclick='tag_sort(this)' title='remove' class='selected'>"+ selected_tags[tid] + "</a>";
        $('#tags').append(row);
    }

    var tags_sort = sort(tags);
    for (var id in tags_sort) {

        var name = tags_sort[id][0];

        if (selected_tags.indexOf(name) >= 0) continue;

        if (name.length > 25) name = name.substring(0, 21) + "..";

        var row = "<a href='javascript:void(0)' onclick='tag_sort(this)' title='" + tags_sort[id][1] + "'>"+ name + "</a>";

        $('#tags').append(row);

        if (id >= 15 - selected_tags.length) break;
    }

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

    courses();
}

function domains_tag_sort(clicked) { //sorry

    if (domains_selected_tags.indexOf(clicked.text) < 0)
        domains_selected_tags.push(clicked.text);

    else domains_selected_tags.splice(domains_selected_tags.indexOf(clicked.text), 1);

    sources();
}
