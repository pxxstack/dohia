//控制层
app.controller('goodsController', function ($scope, $controller, itemCatService,goodsService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.records;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.add = function () {
        goodsService.add($scope.entity).success(
            function (response) {
                if (response.success) {
                    alert("保存成功");
                    $scope.entity={}
                    //重新查询
                   // $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.records;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }


    //定义一个数组来判断商品审核的状态
    $scope.shopStatus = ['未申请', '申请中', '审核通过', '已驳回','取消申请'];
    //定义一个集合来存储分类内容
    $scope.mgItemList=[];
    $scope.findMyItemList=function () {
        itemCatService.findAll().success(
            function (response) {
                //alert("111");
                for(var i=0;i<response.length;i++){
                    $scope.mgItemList[response[i].id]=response[i].name;
                }
                //alert($scope.mgItemList[1])
            }
        )
    }

    $scope.auditOrRejectShop=function (status) {
        //获取选中的复选框
        goodsService.auditOrRejectShop($scope.selectIds,status).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }


});	
