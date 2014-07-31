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

    m_top = localStorage.getItem("m_top") === "true";

//    console.log(m_top);

    if (m_top == null) {

        localStorage.setItem("m_top", true);
        m_top = true
    }
}

var position = (window.pageYOffset || document.documentElement.scrollTop) - (document.documentElement.clientTop || 0);;

var sum = function (a, b) {
    return a + b;
};

edTagApp.controller('mainCtrl', function ($scope, $http, $location) {

    $scope.getBucket = function () {

        return "https://s3-eu-west-1.amazonaws.com/dry-tundra-9556";
//        return "https://s3.amazonaws.com/edtag";
//        return "/assets";
    }

    $scope.showSearch = function () {

        var doc = document.documentElement;
        var top = (window.pageYOffset || doc.scrollTop) - (doc.clientTop || 0);

        if (top <= 30) return;

        var searchBar = angular.element(document.querySelector('.top-menu'));

        document.getElementById("logo").innerHTML = (!m_top) ? "&#9906; &#60;" : "&#9906; &#62;";

        document.getElementById("search").focus();

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

//            console.log($scope.sortedTags);
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
});


edTagApp.controller('linksCtrl', function ($scope, $http) {

    fill($scope, $http, '/api/links/list');

    $scope.favoriteLink = function (data) {

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

function filter($scope, tag) {

    if ($scope.selectedTags.indexOf(tag.name) < 0)
        $scope.selectedTags.push(tag.name);

    else $scope.selectedTags.splice($scope.selectedTags.indexOf(tag.name), 1);

    console.log($scope.selectedTags);

//    console.log($scope.data)

    var newData = [];

    angular.forEach($scope.data, function (data) {

        var dataTagsKeys = [];
        angular.forEach(data.tags, function (dataTag) {
            dataTagsKeys.push(dataTag.name)
        });

//        console.log(dataTagsKeys);

        var remove = false;
        angular.forEach($scope.selectedTags, function (tag) {

//            console.log(tag);

            if (!(dataTagsKeys.indexOf(tag) > -1)) {
                remove = true;
//                console.log('[remove]');
            }
        });

        if (!remove) {
            newData.push(data);
//            console.log('[add]');
        }

//        if (remove) {
//            $scope.data.splice($scope.data.indexOf(data, 1));
//        }
    })

//    $scope.$apply(function(){ //let angular know the changes
    console.log($scope.dataList);
    $scope.dataList = getData(newData);
//    console.log($scope.dataList);
//        $scope.apply();
//    });

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



//        angular.forEach($scope.bundles, function (bundle) {
//            angular.forEach(bundle.tags, function (tag) {
//                if (tag.weight > 50 && tag.weight < 100 && tag.name.length > 3) allTags.push(tag)
//            })
//        })
//        var reducedTags = _.chain(allTags).groupBy('name').map(function (v) {
//            return {name: v[0].name, weight: _.pluck(v, "weight").reduce(sum)};
//        }).value();
//
//        $scope.sortedTags = _.sortBy(reducedTags, 'weight').reverse()

    console.log($scope.dataList)

//    $scope.$apply()
}

window.onscroll = function (e) {

    var doc = document.documentElement;
    var scroll = (window.pageYOffset || doc.scrollTop) - (doc.clientTop || 0);



    if (scroll > 30) {

//        if (m_top)
//            document.getElementById("logo").innerHTML = "&#9906; &#62;";
//
//        else
//            document.getElementById("logo").innerHTML = "&#9906; &#60;";


        if (scroll > position) {

            //scroll down. show nothing
//
            document.getElementById("logo").innerHTML = "&#9906; &#62;";
            document.querySelectorAll(".top-menu")[0].className = "top-menu";
            //hide menu here

        } else {

            //scroll up. show nothing

//            console.log(m_top);

            if (m_top) {

                document.querySelectorAll(".top-menu")[0].className = "top-menu m-top";
//                document.querySelectorAll(".top-menu")[0].toggleClass("");
                document.getElementById("logo").innerHTML = "&#9906; &#60;";
                //show menu

            } else {

                document.querySelectorAll(".top-menu")[0].className = "top-menu";
                document.getElementById("logo").innerHTML = "&#9906; &#62;";
            }
        }

        position = scroll;

    } else
        document.getElementById("logo").innerHTML = "&#60; &#62;";


}

console.log("Menu autoshow for upscroll: " + m_top)
