var edTagApp = angular.module('edTagApp', []);

edTagApp.controller('mainCtrl', function ($scope, $http) {
  $http({method: 'GET', url: '/pages'}).
    success(function(data, status, headers, config) {
      $scope.links = data;

      // this callback will be called asynchronously
      // when the response is available
    }).
    error(function(data, status, headers, config) {
      // called asynchronously if an error occurs
      // or server returns response with an error status.
    });

  $scope.getDomain = function(url){
    return url.replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
  }
  $scope.getWordsCount = function(words){
    return Math.floor(words/100)
  }
});

edTagApp.controller('srcCtrl', function ($scope, $http) {
  $scope.tags = []
  $http({method: 'GET', url: '/alldomains'}).
    success(function(data, status, headers, config) {
      $scope.links = data;
      angular.forEach(data, function(item){
        var tags = item.tags;
      //  var obj = eval('{' + item.tags + '}');
        //for(tag in item.tags){
          console.log(typeof(tags))
          //if (weight > 5) $scope.tags.push(tag)
        //}
      })

      // this callback will be called asynchronously
      // when the response is available
    }).
    error(function(data, status, headers, config) {
      // called asynchronously if an error occurs
      // or server returns response with an error status.
    });
  $scope.getDomain = function(url){
    return url.replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
  }
  $scope.getWordsCount = function(words){
    return Math.floor(words/100)
  }
});



// function logo() {
//
// }
//
// function signInCallback(authResult) {
//
//     if (authResult['status']['signed_in']) {
//
//
//         document.getElementById('signOutButton').setAttribute('style', 'display: inline');
//         document.getElementById('signInButton').setAttribute('style', 'display: none');
//
//         $.post("/auth", { code: authResult['code'] });
//
// //        console.log(authResult);
//
// //        pages();
//         domains();
//
//         return;
//
// //        $.ajax({
// //            type: 'POST',
// //            url: '/signInCallback',
// //            contentType: 'application/octet-stream; charset=utf-8',
// //            processData: true,
// //            data: authResult['code'],
// //            success: function(result) {
// //
// //                console.log(result);
// //
// //                if (result['profile']){
// //
// //
// //
// //                } else {
// //
// //                    $('#main').html('Failed to make a server-side call. Check your configuration and console.');
// //                }
// //            }
// //        });
//
//
//     } else {
//
//         // Update the app to reflect a signed out user
//         // Possible error values:
//         //   "user_signed_out" - User is signed-out
//         //   "access_denied" - User denied access to your app
//         //   "immediate_failed" - Could not automatically log in the user
//
//         document.getElementById('signInButton').setAttribute('style', 'display: inline');
//         document.getElementById('signOutButton').setAttribute('style', 'display: none');
//         console.log('Sign-in state: ' + authResult['error']);
//     }
//
// //    pages();
//     domains();
// }
//
// var fixmeTop = 48;                              // Get initial position
// $(window).scroll(function() {                   // Assign scroll event listener
//
//     return;
//
//     var currentScroll = $(window).scrollTop();  // Get current position
//     if (currentScroll >= fixmeTop) {            // Make it fixed if you've scrolled to it
//
//         $('#domains').css({
//             position: 'fixed',
//             top: '20px',
//             left: '20px'
//         });
//
//         $('#tags').css({
//             position: 'fixed',
//             top: '20px',
//             left: '90px'
//         });
//
//
//     } else {
//
//         $('#domains').css({
//             position: 'absolute',
//             top: '67px',
//             left: '20px'
//         });
//
//         $('#tags').css({
//             position: 'absolute',
//             top: '67px',
//             left: '90px'
//         });
//     }
// });
