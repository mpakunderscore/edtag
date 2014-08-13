edTagApp.controller('tagsCtrl', function ($scope, $http) {

    fill($scope, $http, '/api/tags');

    $scope.removeTag = function (tag) {

        $http({method: 'GET', url: '/tag/remove/' + tag}).
            success(function (data, status, headers, config) {
            }).
            error(function (data, status, headers, config) {
            });
    }
});
