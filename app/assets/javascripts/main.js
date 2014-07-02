var edTagApp = angular.module('edTagApp', []);

edTagApp.controller('mainCtrl', function ($scope, $http) {
    var allTags = []
    var sum = function(a,b){ return a+b; };
    $scope.selected_tags = {}
    $scope.allTags = {}
    $scope.addToCourse = function(obj){
        console.log(obj.webData)

    }
    $scope.showSearch = function() {

        var searchBar = angular.element(document.querySelector('.top-menu'));

        document.getElementById("logo").children[0].innerHTML = "&#9906; &#60;"; //TODO make this if only hide = false
        document.getElementById("search").focus();

        console.log(searchBar.toggleClass('m-top'));
    }

    $http({method: 'GET', url: '/bundles/list'}).
        success(function (data, status, headers, config) {

            $scope.bundles = data;

            angular.forEach(data, function (bundle) {

                console.log(bundle);
		        angular.forEach(bundle.tags, function (tag) {

//                    tag.weight = tag.weight;
//                    if (tag.weight > 50 && tag.weight < 100 && tag.name.length > 3) {
                      allTags.push(tag)
//                    console.log(tag);
//                    }

//                    (allTags[tag.name] != null) ? allTags[tag.name] += parseInt(tag.weight) : allTags[tag.name] = parseInt(tag.weight);
                })
            })


            var reducedTags = _.chain(allTags).groupBy('name').map(function(v){
              return {name:v[0].name,weight:_.pluck(v, "weight").reduce(sum)};
            }).value();

            $scope.sortedTags =_.sortBy(reducedTags, 'weight').reverse()

//            $scope.sortedTags = sort(allTags).slice(0, 15)

        }).
        error(function (data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });

    $scope.getDomain = function (url) {
        return url.replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
    };

    $scope.searchText = ''

    $scope.getCount = function (words) {
        return Math.floor(words / 100)
    }
});

function sort(map) {

    var sortable = [];
    for (var key in map)
        if (map[key] != null)
            sortable.push([key, map[key]]);

    sortable.sort(function(b, a) {return a[1] - b[1]});

    return sortable
}

edTagApp.controller('srcCtrl', function ($scope, $http) {

    $scope.tags = [];
    $http({method: 'GET', url: '/alldomains'}).
        success(function (data, status, headers, config) {

            $scope.links = data;

            angular.forEach(data, function (item) {
                var tags = item.tags;
                //  var obj = eval('{' + item.tags + '}');
                //for(tag in item.tags){
                console.log(typeof(tags));
                //if (weight > 5) $scope.tags.push(tag)
                //}
            });

            // this callback will be called asynchronously
            // when the response is available
        }).
        error(function (data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });

    $scope.getDomain = function (url) {
        return url.replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
    };

    $scope.getCount = function (words) {
        return Math.floor(words / 100)
    }
});

window.onscroll = function (e) {

    var doc = document.documentElement;
    var top = (window.pageYOffset || doc.scrollTop)  - (doc.clientTop || 0);

    if (top > 30) {

        document.getElementById("logo").children[0].innerHTML = "&#9906; &#62;";
        //TODO hide top-menu here

    } else
        document.getElementById("logo").children[0].innerHTML = "&#60; &#62;";
}