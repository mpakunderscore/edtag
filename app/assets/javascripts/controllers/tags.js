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

var sum = function (a, b) {
    return a + b;
};

function fill($scope, $http, url) {

    $http({method: 'GET', url: url}).
        success(function (data, status, headers, config) {

            $scope.data = data;

            $scope.dataList = getData(data);

            $scope.sortedTags = getCategories(data);

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

function getCategories(data) {

    var allTags = [];

    angular.forEach(data, function (tag) {

        angular.forEach(tag.tags, function (category) {

            allTags.push(category)
        })
    })

    var reducedTags = _.chain(allTags).groupBy('name').map(function (v) {
        return {name: v[0].name, weight: _.pluck(v, "weight").reduce(sum)};
    }).value();

    return _.sortBy(reducedTags, 'weight').reverse();
}
