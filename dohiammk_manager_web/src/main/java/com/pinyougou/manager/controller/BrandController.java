package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;


    @RequestMapping("/findAll.do")
    public List<TbBrand> findALL(){
        //System.out.println("进来了吗？");
        return brandService.findAll();
    }

    @RequestMapping("/findByPage.do")
    public PageResult findByPage(int pageNum,int pageSize){
        //System.out.println(pageNum);
        //System.out.println(pageSize);
        return brandService.findByPage(pageNum,pageSize);
    }

    @RequestMapping("/addBrand.do")
    public Result addBrand(@RequestBody TbBrand tbBrand){
        try {
            brandService.addBrand(tbBrand);
            return new Result(true,"添加品牌成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加品牌失败");
        }
    }

    @RequestMapping("/findById.do")
    public TbBrand findById(long id){
        return brandService.findById(id);
    }

    @RequestMapping("/updateBrand.do")
    public Result updateBrand(@RequestBody TbBrand tbBrand){
        try {
            brandService.updateBrand(tbBrand);
            return new Result(true,"修改品牌成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改品牌成功");
        }
    }

    @RequestMapping("/deleteBrand.do")
    public Result deleteBand(Long[] ids){
        try {
            brandService.deleteBrand(ids);
            return new Result(true,"删除品牌成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"删除品牌失败");
        }
    }

    //条件查询
    @RequestMapping("/findSearchPage.do")
    public PageResult findSearchPage(@RequestBody TbBrand brand,int pageNum,int pageSize){
        //System.out.println("品牌进来了吗？");
        PageResult pageResult = brandService.findSearchPage(brand, pageNum, pageSize);
        return pageResult;

    }

    //查询品牌信息 封装成select2支持的数据
    @RequestMapping("/selectOptionList.do")
    public List<Map> selectOptionList(){
        List<Map> selectOptionList = brandService.selectOptionList();
        return selectOptionList;
    }
}
