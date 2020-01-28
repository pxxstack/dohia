//服务层
app.service('searchService',function($http){

    //通过搜素条件查询
    this.search=function(searchMap){
        return $http.post('itemSearch/search.do',searchMap);
    }

});
