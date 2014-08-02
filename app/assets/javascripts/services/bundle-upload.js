edTagApp.service('bundleUpload', ['$http', '$location', function ($http, $location) {

    this.uploadFileToUrl = function(file, bundle, uploadUrl){

        var fd = new FormData();
        fd.append('file', file);
        fd.append('title', bundle.title);
        fd.append('description', bundle.description);
        fd.append('urls', bundle.links);

        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(data){

            console.log(data.id);
//            $location.path();
            window.location.href = '/bundle/' + data.id;
        })
        .error(function(data){
        });
    }
}]);
