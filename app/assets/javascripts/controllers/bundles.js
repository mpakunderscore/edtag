edTagApp.controller('bundleGenerationCtrl', function($scope, fileReader, $http, bundleUpload) {
    //console.log(fileReader)
    //var acceptHeader = 'application/json; charset=utf-8';
    $scope.bundle = {
        title: 'Test bundle, please ignore',
        description: '',
        url: "http://en.wikipedia.org/wiki/Special:Random",
        links: []
    }

    $scope.getFile = function () {
        fileReader.readAsDataUrl($scope.file, $scope).then(function(result) {
            $scope.imageSrc = result;
        });
    };



    $scope.analyse = function(){
        var tempLinks = [];
        $http.get(
            '/analyze/page/links',
            {
                params: { 'url': $scope.bundle.url }
            }
        )
        .success(function(data, status, headers, config) {
            angular.forEach(data, function(value, key) {
                tempLinks.push(value.url)
            });
            $scope.bundle.links = tempLinks.join("\n");
        })
        .error(function(data, status, headers, config) {
            console.log(data);
        });
    };

    $scope.submitBundle = function(){
        var file = $scope.myFile;
        var uploadUrl = '/api/bundle/add';
        bundleUpload.uploadFileToUrl(file, $scope.bundle, uploadUrl);
    };

});
