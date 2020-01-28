app.controller("contentController", function ($scope, contentService) {


    $scope.contentList=[];
    $scope.findByCategoryId = function (categoryId) {
        contentService.findByCategoryId(categoryId).success(
            function (response) {
                $scope.contentList[categoryId] = response;
            }
        );
    };

    $scope.forSearch=function () {
        location.href="http://localhost:9105/search.html#?keyWords="+$scope.keyWords;

    }






});