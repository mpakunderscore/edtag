var edTagApp = angular.module('edTagApp', []);

edTagApp.controller('mainCtrl', function ($scope, $http) {

    $http({method: 'GET', url: '/pages'}).
        success(function (data, status, headers, config) {
            $scope.links = data;

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

    $scope.searchText = '';

    $scope.getCount = function (words) {
        return Math.floor(words / 100)
    }
});

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