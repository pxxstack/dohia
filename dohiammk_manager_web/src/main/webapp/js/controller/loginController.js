app.controller("loginController", function ($scope, $controller,loginService ) {
    //伪继承 共用$scope
    $controller('baseController',{$scope:$scope});


    $scope.findLoginName = function () {
        loginService.findLoginName().success(
            function (response) {
                $scope.loginName = response.loginName;
            }
        );
    };





});