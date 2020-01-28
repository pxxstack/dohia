var app = angular.module("pinyougou", []);

// $sce服务，执行过滤器方法
app.filter('trustHtml',['$sce',function($sce){
    return function(data){//data表示原生的html元素
        return $sce.trustAsHtml(data);//返回把原生的html元素设置为信任或者渲染
    }
}]);
