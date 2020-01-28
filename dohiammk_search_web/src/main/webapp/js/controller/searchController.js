//控制层
app.controller('searchController', function ($scope,$location, searchService) {


    //将搜索条件封装成map集合
    $scope.search = function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
                //调用分页查询
                setMyPages();
            }
        );

    }

    //$scope.searchMap={keyWords:{},category:{},brandList:[],specList:[]}
    $scope.searchMap = {
        'keyWords': '',
        'category': '',
        'brand': '',
        'spec': {},
        'price': '',
        'myPageNum': 1,
        'myPageSize': 40,
        'sort':'',
        'sortField':''
    };

    //添加条件 添加搜索项，key表示品牌brand或者商品分类category和规格名称，value表示相对应的选项
    $scope.addSearchItem = function (key, value) {
        if (key == 'brand' || key == 'category' || key == 'price') {
            //其实这个操作就是向搜索对象中添加值
            $scope.searchMap[key] = value;
        } else {//如果是规格
            $scope.searchMap.spec[key] = value;//把值加进spec对象中
        }

        $scope.search();

    }

    $scope.removeSearchItem = function (key) {
        //其实这个操作就是向搜索对象中添加值
        if (key == 'brand' || key == 'category' || key == 'price') {
            //如果是品牌或者商品分类的话
            $scope.searchMap[key] = '';//置空处理
        } else {//如果是规格
            delete $scope.searchMap.spec[key];//删除相应值
        }

        $scope.search();

    }

    //清除品牌 规格 分类等
    /* $scope.clearInformation=function () {
         $scope.searchMap={'category':'','brand':'','spec':{},'price':''}
     }*/

    //进行分页设置


    setMyPages = function () {
        //设置分页标签栏
        $scope.setPageLabel = [];
        var myfirstPage = 1;//设置首页
        var myendPage = $scope.resultMap.totalPage;//设置尾页
        $scope.firstDor=false;//前面有点
        $scope.lastDor=false;//后面有点
        if ($scope.resultMap.totalPage > 5) {
            if ($scope.searchMap.myPageNum < 4) {
                myendPage = 5;
                $scope.lastDor=true;
            }else if ($scope.searchMap.myPageNum == $scope.resultMap.totalPage ) {
                myfirstPage = $scope.searchMap.myPageNum - 4;
                $scope.firstDor=true;

            }else if ($scope.searchMap.myPageNum == $scope.resultMap.totalPage-1 ) {
                myfirstPage = $scope.searchMap.myPageNum - 3;
                $scope.firstDor=true;


            } else {
                myfirstPage = $scope.searchMap.myPageNum - 2;
                myendPage = $scope.searchMap.myPageNum + 2;
                $scope.firstDor=true;
                $scope.lastDor=true;
            }
        }
        //循环添加每页页数
        for (var i = myfirstPage; i <= myendPage; i++) {
            $scope.setPageLabel.push(i);
        }


    }

    //通过分页页数查询
    $scope.searchByPage=function (pageNum) {
        //当页面数不符合逻辑时 返回
        if(pageNum<1||pageNum>$scope.resultMap.totalPage){
            return
        }else {
            //将当前页面传递给myPageNum
            //alert(pageNum);
            $scope.searchMap.myPageNum=pageNum;
            //执行查询
            $scope.search();
        }
    }

    //根据前台传输的内容进行赋值然后排序查询
    $scope.searchSort=function(mysort,mysortfield){
        $scope.searchMap.sort=mysort;
        $scope.searchMap.sortField=mysortfield;
        $scope.search();
    }

    //如果关键字是品牌 则不显示品牌
    $scope.keyWordIsBrand=function () {
        for (var i=0;i<$scope.resultMap.brandList.length;i++){
            //alert($scope.searchMap.keyWords);
            if($scope.searchMap.keyWords.indexOf($scope.resultMap.brandList[i].text)>=0){//如果包含
                //alert($scope.resultMap.brandList[i].text);
                //alert("1");
                return true;
            }
        }
        //alert("222");
        return false;
        //alert("111");
    }
    
    //接收请求参数
    $scope.loadKeyWordsForSearch=function () {
        $scope.searchMap.keyWords=$location.search()['keyWords'];
        //alert($scope.searchMap.keyWords);
        $scope.search();
    }


});
