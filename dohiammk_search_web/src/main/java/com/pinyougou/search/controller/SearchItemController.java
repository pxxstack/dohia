package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.SearchItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemSearch")
public class SearchItemController {

    @Reference
    private SearchItemService searchItemService;


    @RequestMapping("/search")
    public Map<String, Object> search(@RequestBody Map searchMap){
        //System.out.println("3333"+searchMap.get("keyWords"));
        return searchItemService.search(searchMap);
    }
}
