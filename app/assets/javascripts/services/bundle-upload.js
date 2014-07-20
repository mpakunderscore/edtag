edTagApp.service('bundleUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, bundle, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        fd.append('title', bundle.name);
        fd.append('description', bundle.description);
        fd.append('urls', bundle.links);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(){
        })
        .error(function(){
        });
    }
}]);
