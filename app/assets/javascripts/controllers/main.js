var m_top = true;

var sum = function (a, b) {
    return a + b;
};

edTagApp.controller('mainCtrl', function ($scope, $http, $location) {

    var allTags = [];
    $scope.selected_tags = {};
    $scope.allTags = [];

    $scope.showSearch = function () {

        var doc = document.documentElement;
        var top = (window.pageYOffset || doc.scrollTop) - (doc.clientTop || 0);

        if (top <= 30) return;

        var searchBar = angular.element(document.querySelector('.top-menu'));

        document.getElementById("logo").innerHTML = (m_top) ? "&#9906; &#60;" : "&#9906; &#62;";

        document.getElementById("search").focus();

        m_top = !m_top;

        searchBar.toggleClass('m-top');
    }

    $scope.getClass = function (path) {

        if ($location.absUrl().indexOf(path) > -1) {
            return "current-page"
        } else {
            return ""
        }
    }

    $scope.searchText = ''
    $scope.filterByTag = function (link) {

        console.log(link)
        var bundles = [];
        var tags = []

        angular.forEach($scope.bundles, function (bundle) {
            angular.forEach(bundle.tags, function (tag) {
                if (link.tag.name == tag.name) {
                    bundles.push(bundle);

                }

            })
        })
        $scope.bundles = bundles

        angular.forEach($scope.bundles, function (bundle) {
            angular.forEach(bundle.tags, function (tag) {
                if (tag.weight > 50 && tag.weight < 100 && tag.name.length > 3) allTags.push(tag)
            })
        })
        var reducedTags = _.chain(allTags).groupBy('name').map(function (v) {
            return {name: v[0].name, weight: _.pluck(v, "weight").reduce(sum)};
        }).value();
        $scope.sortedTags = _.sortBy(reducedTags, 'weight').reverse()
        console.log($scope.sortedTags)
    }
});

function fill($scope, $http, url) {

    $http({method: 'GET', url: url}).
        success(function (data, status, headers, config) {

            $scope.data = data;

            $scope.sortedTags = getTags(data);

//            console.log($scope.sortedTags);
        }).
        error(function (data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });

    $scope.getDomain = function (url) {
        return url.replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
    };
}

function getTags(data) {

    var allTags = [];

    angular.forEach(data, function (link) {

        angular.forEach(link.tags, function (tag) {

//            if (tag.weight > 50 && tag.weight < 100 && tag.name.length > 3)
                allTags.push(tag)
        })
    })

    var reducedTags = _.chain(allTags).groupBy('name').map(function (v) {
        return {name: v[0].name, weight: _.pluck(v, "weight").reduce(sum)};
    }).value();

    return _.sortBy(reducedTags, 'weight').reverse().slice(0, 14);
}

edTagApp.controller('bundlesCtrl', function ($scope, $http) {

    fill($scope, $http, '/api/bundles/list');
});

edTagApp.controller('bundleCtrl', function ($scope, $http, $location) {

    var id = $location.absUrl().split("/")[$location.absUrl().split("/").length-1]; //TODO -_- it's 03:21, sorry

    $http({method: 'GET', url: '/api/bundle/' + id}).
        success(function (data, status, headers, config) {

            $scope.data = data.webDataList;

            $scope.sortedTags = getTags(data.webDataList);

//            console.log($scope.data);
        }).
        error(function (data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });

    $scope.getDomain = function (url) {
        return url.replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
    };
});


edTagApp.controller('linksCtrl', function ($scope, $http) {

    fill($scope, $http, '/api/links/list');

    $scope.favoriteLink = function (data) {

        console.log(data);

        $http({method: 'GET', url: '/favorite?webDataId=' + data}).
            success(function (data, status, headers, config) {
            }).
            error(function (data, status, headers, config) {
            });
    }
});

edTagApp.controller('favoritesCtrl', function ($scope, $http) {

    fill($scope, $http, '/api/links/favorites');
});

window.onscroll = function (e) {

    var doc = document.documentElement;
    var top = (window.pageYOffset || doc.scrollTop) - (doc.clientTop || 0);

    if (top > 30) {

        if (m_top)
            document.getElementById("logo").innerHTML = "&#9906; &#62;";

        else
            document.getElementById("logo").innerHTML = "&#9906; &#60;";
        //TODO hide top-menu here

    } else
        document.getElementById("logo").innerHTML = "&#60; &#62;";
}

