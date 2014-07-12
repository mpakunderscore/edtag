edTagApp.controller('uploadController', function($scope, fileReader, $http) {
     console.log(fileReader)

    $scope.bundle = {
        name: 'Search engines',
        links: [
            {url: "http://ya.ru"},
            {url: "http://gogle.com"},
            {url: "http://duckduckgo.com"}
        ],
        img: null

    };

    $scope.addLink = function(){

        var count = $scope.bundle.links.length,
            valid = 0;

        $scope.bundle.links.forEach(function(value){
            if (value.url){
                 valid++;
            }
        });

        if (valid == count){
            $scope.bundle.links.push({url:''}); // This should just add an empty input
                                   // For some reason it copies the last typed input
            // If you change this to .push(null),
            // it will work for the first change(), but not thereafter
        }


    }



    $scope.getFile = function () {
        fileReader.readAsDataUrl($scope.file, $scope).then(function(result) {
            $scope.imageSrc = result;
        });
    };

    $scope.submitBundle = function(){
        $http.post('/api/bundle/add',$scope.bundle,{'Content-Type': 'multipart/form-data'})
            .success(function(data, status, headers, config) {
              // this callback will be called asynchronously
              // when the response is available
            })
            .error(function(data, status, headers, config) {
              // called asynchronously if an error occurs
              // or server returns response with an error status.
            });

    };

});
