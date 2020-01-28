app.service("brandService", function ($http) {
    this.findAll = function () {
        return $http.get('../brand/findAll.do');
    };
    this.findByPage = function (page, size) {
        return $http.get('../brand/findByPage.do?pageNum=' + page + '&pageSize=' + size);
    };
    this.addBrand = function (entity) {
        return $http.post('../brand/addBrand.do', entity);
    };
    this.updateBrand = function (entity) {
        return $http.post('../brand/updateBrand.do', entity);
    };
    this.findById = function (id) {
        return $http.get('../brand/findById.do?id=' + id);
    };
    this.delBrandById = function (ids) {
        return $http.get('../brand/deleteBrand.do?ids=' + ids);
    };
    this.search = function (page, size, searchEntity) {
        return $http.post('../brand/findSearchPage.do?pageNum=' + page + '&pageSize=' + size, searchEntity);
    };
    this.selectOptionList = function () {
        return $http.get('../brand/selectOptionList.do');
    }
});