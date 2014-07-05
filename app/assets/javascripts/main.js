var edTagApp = angular.module('edTagApp', []);

edTagApp.controller('mainCtrl', function ($scope, $http) {

    var allTags = []
    var sum = function(a,b){ return a+b; };

    $scope.selected_tags = {};
    $scope.allTags = {}

    $scope.showSearch = function() {

        var searchBar = angular.element(document.querySelector('.top-menu'));

        document.getElementById("logo").children[0].innerHTML = "&#9906; &#60;";
        document.getElementById("search").focus();
    }

    $http({method: 'GET', url: '/bundles/list'}).
        success(function (data, status, headers, config) {

            $scope.bundles = data;

            angular.forEach(data, function (bundle) {

		        angular.forEach(bundle.tags, function (tag) {

//                    if (tag.weight > 50 && tag.weight < 100 && tag.name.length > 3)
                        allTags.push(tag)
                })
            })

            var reducedTags = _.chain(allTags).groupBy('name').map(function(v){
              return {name:v[0].name,weight:_.pluck(v, "weight").reduce(sum)};
            }).value();

            $scope.sortedTags =_.sortBy(reducedTags, 'weight').reverse()
        }).
        error(function (data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });

    $scope.searchText = ''

});

edTagApp.controller('linksCtrl', function ($scope, $http) {

    var allTags = []
    var sum = function(a,b){ return a+b; };

    $scope.selected_tags = {};
    $scope.allTags = {}

    $scope.showSearch = function() {

        var searchBar = angular.element(document.querySelector('.top-menu'));

        document.getElementById("logo").children[0].innerHTML = "&#9906; &#60;";
        document.getElementById("search").focus();
    }

    $scope.tags = [];
    $http({method: 'GET', url: '/links/list'}).
        success(function (data, status, headers, config) {

            $scope.links = data;

            angular.forEach(data, function (link) {

                angular.forEach(link.tags, function (tag) {

//                    if (tag.weight > 50 && tag.weight < 100 && tag.name.length > 3)
                    allTags.push(tag)
                })
            })

            var reducedTags = _.chain(allTags).groupBy('name').map(function(v){
                return {name:v[0].name,weight:_.pluck(v, "weight").reduce(sum)};
            }).value();

            $scope.sortedTags =_.sortBy(reducedTags, 'weight').reverse()
        }).
        error(function (data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });

    $scope.getDomain = function (url) {
        return url.replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
    };
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