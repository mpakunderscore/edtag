edTagApp.controller('uploadController', function($scope, fileReader, $http) {
     console.log(fileReader)
    $scope.bundle = {
        name: 'Test bundle',
        url: "http://ya.ru",
        img: null

    }
    $scope.addLink = function(){
        $scope.bundle.links.push({url : ""});
    }
     $scope.getFile = function () {
        fileReader.readAsDataUrl($scope.file, $scope).then(function(result) {
            $scope.imageSrc = result;
        });
    };
    $scope.analyse = function(){
        $http.get('/analyze/page/links',{params: {url: $scope.bundle.url}},{headers: {'Accept': 'application/json; charset=utf-8','Accept-Charset': 'charset=utf-8'}})
        .success(function(data, status, headers, config) {
            var links = []
            angular.forEach(data, function(value, key) {
                console.log(value.url);
                links.push(value.url)
            });
            $scope.bundle.links = links.join("\n");
        })
        .error(function(data, status, headers, config) {
            console.log(data);
        });
    };

    $scope.submitBundle = function(){
            $http({
                url: '/api/bundle/add',
                method: "POST",
                data: $scope.bundle
            })
            .then(function(response) {
                    console.log('success', response)
                },
                function(response) { // optional
                    console.log('failure', response)
                }
            );
    };

});
