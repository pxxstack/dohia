app.controller('baseController',function ($scope) {
    //分页控件配置currentPage:当前页   totalItems :总记录数  itemsPerPage:每页记录数  perPageOptions :分页选项  onChange:当页码变更后自动触发的方法
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [5, 10, 15, 20, 25],
        onChange: function () {
            $scope.reloadList();
        }
    };


    //刷新列表
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    //创建一个关于id的数组 一旦id被选中就添加id 如果id选中之后又取消选中就删除id
    $scope.selectIds = [];


    //将被选中的id添加进数组
    $scope.changeSelected = function ($event, id) {
        //alert(id);
        // alert($scope.mySelectedId);
        if ($event.target.checked) {
            $scope.selectIds.push(id);
        } else {
            var index = $scope.selectIds.indexOf(id);//查找值的 位置
            $scope.selectIds.splice(index, 1);//参数1：移除的位置 参数2：移除的个数
        }
    };


    // 查询条件初始化
    $scope.searchEntity = {};


    //将会写的字符串转换成json对象
    $scope.stringToJson=function (jsonString,key) {
        var json=JSON.parse(jsonString);
        var result='';
        for(var i=0;i<json.length;i++){
            if(i>0){
                result += ','
            }
            result +=json[i][key]
        }
        return result;
    }










});