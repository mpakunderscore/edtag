edTagApp.controller('uploadController', function($scope, fileReader, $http) {
     console.log(fileReader)
    $scope.bundle = {
        name: 'Test bundle',
        links: [
            {url: "http://ya.ru"},
        ],
        img: null

    }
    $scope.addLink = function(){
        $scope.bundle.links.push({url : ""});
    }
    $scope.getFile = function () {
        $scope.progress = 0;
        fileReader.readAsDataUrl($scope.file, $scope).then(function(result) {
            $scope.imageSrc = result;
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
