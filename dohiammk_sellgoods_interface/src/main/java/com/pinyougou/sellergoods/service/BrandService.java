package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;
import entity.Result;

import java.util.List;
import java.util.Map;

public interface BrandService {
    List<TbBrand> findAll();

    /**
     * 当前页数pageNum
     * 每页显示条数pageSize
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult findByPage(int pageNum,int pageSize);
    void addBrand(TbBrand brand);
    TbBrand findById(long id);
    void updateBrand(TbBrand brand);
    void deleteBrand(Long[] ids);
    PageResult findSearchPage(TbBrand brand,int pageNum, int pageSize);
    List<Map> selectOptionList();
}
