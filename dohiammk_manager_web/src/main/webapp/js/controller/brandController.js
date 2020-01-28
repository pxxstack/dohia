app.controller("brandController", function ($scope, $controller, brandService) {
    //伪继承 共用$scope
    $controller('baseController',{$scope:$scope});


    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };



    $scope.search = function (page, size) {
        brandService.search(page, size,$scope.searchEntity).success(
            function (response) {
                // $scope.searchEntity={};
                //显示当前页面数据
                $scope.list = response.records;
                //显示总记录数
                $scope.paginationConf.totalItems = response.total;

            }
        )

    };

    //当使用条件查询时  次方法已经作废啦
    $scope.findByPage = function (page, size) {
        brandService.findByPage(page, size).success(
            function (response) {
                //显示当前页面数据
                $scope.list = response.records;
                //显示总记录数
                $scope.paginationConf.totalItems = response.total;
            }
        )
    };

    //添加品牌或修改品牌然后保存
    $scope.save = function () {
        // var methodName = "addBrand.do";
        //alert($scope.entity.name);
        var object = null;

        if ($scope.entity.id != null) {
            object = brandService.updateBrand($scope.entity);
        } else {
            object = brandService.addBrand($scope.entity);
        }
        object.success(
            function (response) {
                //alert(1);
                if (response.success) {
                    $scope.reloadList();
                } else {
                    alert(response.message)
                }
            }
        )
    };
    //通过id回写品牌信息
    $scope.findById = function (id) {
        brandService.findById(id).success(
            function (response) {
                $scope.entity = response;

            }
        )

    };

    //将被选中id的品牌删除
    $scope.delBrandById = function () {
        if ($scope.selectIds.length == 0) {
            alert("请至少选择一项进行删除")
        } else {
            if (confirm("您确定要删除吗")) {
                brandService.delBrandById($scope.selectIds).success(
                    function (response) {
                        if (response.success) {
                            $scope.reloadList();
                            $scope.selectIds = [];
                        } else {
                            alert(response.message)
                        }

                    }
                )
            }

        }

    };




});