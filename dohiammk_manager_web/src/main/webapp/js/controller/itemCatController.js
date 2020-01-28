 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.records;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
    $scope.findOne=function(parentId){
        itemCatService.findOne(parentId).success(
            function(response){
                //alert(1);
                $scope.list= response;
            }
        );
    }
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.records;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//定义一个分类导航级别
	$scope.grade=1;
	//定义一个改变分类级别的方法
	$scope.setGrade=function (value) {
        $scope.grade=value;
    }

    //设置不同级别展示的分类name
	$scope.searchNextGrade=function (pentity) {
		if($scope.grade==1){
            $scope.item_entity1=null;
			$scope.item_entity2=null;
		}

        if($scope.grade==2){
            $scope.item_entity1=pentity;
            $scope.item_entity2=null;
        }
        if($scope.grade==3){
            // $scope.item_entity1=pentity;
            $scope.item_entity2=pentity;
        }

        itemCatService.findOne(pentity.id).success(
            function(response){
                //alert(1);
                $scope.list= response;
            }
        );


    }
    
});	
