function supports_html5_storage() {
    try {
        return 'localStorage' in window && window['localStorage'] !== null;
    } catch (e) {
        return false;
    }
}

console.log("Supports html5 storage: " + supports_html5_storage());

var m_top; //TODO

if (supports_html5_storage()) {

    m_top = localStorage.getItem("m_top");

//    console.log(m_top);

    if (m_top == null) {

        localStorage.setItem("m_top", true);
        m_top = true

    } else {
        m_top = m_top === "true"; // -_-
    }
}

var position = (window.pageYOffset || document.documentElement.scrollTop) - (document.documentElement.clientTop || 0);

var sum = function (a, b) {
    return a + b;
};

edTagApp.directive('ngEnter', function () {
    return function ($scope, $element, $attrs) {
        $element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                $scope.$apply(function (){
//                    $scope.$eval($attrs.ngEnter);
//                    alert(document.getElementById('search').value)
                    var tag = {};
                    tag.name = $scope.searchText;
                    filter($scope.$$childHead, tag);
                    $scope.searchText = ''
                });

                event.preventDefault();
            }
        });
    };
});

edTagApp.controller('mainCtrl', function ($scope, $http, $location) {

    $scope.getBucket = function () {

        return "https://s3-eu-west-1.amazonaws.com/dry-tundra-9556";
//        return "https://s3.amazonaws.com/edtag";
//        return "/assets";
    }

    if (!m_top) angular.element(document.querySelector('header')).toggleClass('m-top');

    $scope.showSearch = function () {

        var doc = document.documentElement;
        var top = (window.pageYOffset || doc.scrollTop) - (doc.clientTop || 0);

        if (top <= 70) return;

        var searchBar = angular.element(document.querySelector('header'));

        document.getElementById("logo").innerHTML = (!m_top) ? "&#9906; &#62;" : "&#9906; &#60;";

//        document.getElementById("search").focus(); //TODO

        m_top = !m_top;

        if (supports_html5_storage())
            localStorage.setItem("m_top", m_top);

        searchBar.toggleClass('m-top');

        console.log("Menu: " + m_top);
    }

    $scope.getClass = function (path) {

        if ($location.absUrl().indexOf(path) > -1) {
            return "current-page"
        } else {
            return ""
        }
    }

    $scope.searchText = ''
    $scope.getDomain = function (url) {
        return url.replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
    };
});

function fill($scope, $http, url) {

    $http({method: 'GET', url: url}).
        success(function (data, status, headers, config) {

            $scope.data = data;

            $scope.dataList = getData(data);

            $scope.sortedTags = getTags(data);

        }).
        error(function (data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });

    $scope.selectedTags = [];

    $scope.filterByTag = function (tag) {
        filter($scope, tag)
    }
}

function getData(data) {
    return data;
}

function getTags(dataList) {

    var allTags = [];

    angular.forEach(dataList, function (data) {

        angular.forEach(data.tags, function (tag) {

            if (tag.name.length < 30)
                allTags.push(tag)
        })
    })

    var reducedTags = _.chain(allTags).groupBy('name').map(function (v) {
        return {name: v[0].name, weight: v.length};
    }).value();

    return _.sortBy(reducedTags, 'weight').reverse();
}

edTagApp.controller('bundlesCtrl', function ($scope, $http) {

    fill($scope, $http, '/api/bundles/list');
});

edTagApp.controller('bundleCtrl', function ($scope, $http, $location) {

    var id = $location.absUrl().split("/")[$location.absUrl().split("/").length-1]; //TODO -_- it's 03:21, sorry

    $http({method: 'GET', url: '/api/bundle/' + id}).
        success(function (data, status, headers, config) {

            $scope.data = data.webDataList;

            $scope.dataList = getData(data.webDataList);

            $scope.sortedTags = getTags(data.webDataList);

//            console.log($scope.data);
        }).
        error(function (data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });

    $scope.selectedTags = [];

    $scope.filterByTag = function (tag) {
        filter($scope, tag)
    }

    $scope.favoriteLink = function (link) {

        $http({method: 'GET', url: '/favorite?webDataId=' + link.id}).
            success(function (data, status, headers, config) {
                angular.element(document.querySelector('#f' + link.id)).addClass('checked');
            }).
            error(function (data, status, headers, config) {
            });

        //TODO make star active here
    }
});

edTagApp.controller('linksCtrl', function ($scope, $http) {

    fill($scope, $http, '/api/links/list');

    $scope.favoriteLink = function (link) {

        $http({method: 'GET', url: '/favorite?webDataId=' + link.id}).
            success(function (data, status, headers, config) {
                angular.element(document.querySelector('#f' + link.id)).addClass('checked');
            }).
            error(function (data, status, headers, config) {
            });

        //TODO make star active here
    }
});

edTagApp.controller('favoritesCtrl', function ($scope, $http) {

    fill($scope, $http, '/api/links/favorites');

    $scope.removeLink = function (link) { //$scope.removeLink = function (Щ) {

        $http({method: 'GET', url: '/favorite/remove?webDataId=' + link.id}).
            success(function (data, status, headers, config) {

                $scope.dataList.splice($scope.dataList.indexOf(link), 1);
            }).
            error(function (data, status, headers, config) {
            });
    }
});

function filter($scope, tag) {

    if ($scope.selectedTags.indexOf(tag.name) < 0)
        $scope.selectedTags.push(tag.name);

    else
        $scope.selectedTags.splice($scope.selectedTags.indexOf(tag.name), 1);


    // now sort our data

    var newData = [];
    angular.forEach($scope.data, function (data) {

        var dataTagsKeys = [];
        angular.forEach(data.tags, function (dataTag) {
            dataTagsKeys.push(dataTag.name)
        });

        var remove = false;
        angular.forEach($scope.selectedTags, function (tag) {

            if (!(dataTagsKeys.indexOf(tag) > -1)) {
                remove = true;
            }
        });

        if (!remove)
            newData.push(data);
    });

    $scope.dataList = getData(newData);

    var newTags = getTags(newData);
    var selectedTags = [];

    angular.forEach(newTags, function (tag) {

        angular.forEach($scope.selectedTags, function (selectedTag) {

            if (tag.name === selectedTag)
                newTags.splice(newTags.indexOf(tag), 1);
        })
    })

    angular.forEach($scope.selectedTags, function (selectedTag) {
        selectedTags.push({name: selectedTag, weight: 0})
    })

    $scope.sortedTags = selectedTags.concat(newTags);
}

window.onscroll = function (e) {

    var doc = document.documentElement;
    var scroll = (window.pageYOffset || doc.scrollTop) - (doc.clientTop || 0);

    if (scroll > 70) {

        if (m_top)
            document.getElementById("logo").innerHTML = "&#9906; &#62;";

        else
            document.getElementById("logo").innerHTML = "&#9906; &#60;";

    } else
        document.getElementById("logo").innerHTML = "&#60; &#62;";
}

console.log("Menu hide: " + m_top)
