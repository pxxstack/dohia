package com.pinyougou.search.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map keyWordsMap) {
        /*Map<String, Object> resultMap = new HashMap<>();
        Query query = new SimpleQuery("*:*");
        //将搜索的条件添加到复制域中
        Criteria criteria = new Criteria("item_keywords").is(keyWordsMap.get("keyWords"));
        query.addCriteria(criteria);
        //System.out.println("111");
        //执行查询条件 将结果封装到实体类TbItem
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        // System.out.println("222");
        resultMap.put("rows", tbItems.getContent());
        return resultMap;*/
        String keyWords =(String)keyWordsMap.get("keyWords");
        //删除空格
        keyWordsMap.put("keyWords",keyWords.replace("",""));
        Map<String, Object> map = new HashMap<>();
        //调用高亮显示的集合，将结果追加到map集合中
        map.putAll(searchList(keyWordsMap));
        //将分类结果放入返回集合中
        List categoryList = findCategoryList(keyWordsMap);
        map.put("categoryList", categoryList);
        //判断从前台是否查询了分类，如果没有查询分类，则默认按照第一个查询品牌和规格，如果查询了，则按查询的分类来查询品牌和规格
        String categoryStr = (String) keyWordsMap.get("category");
        if (!"".equals(categoryStr)) {
            Map<String, Object> brandAndSpecMap = searchBrandAndSpec(categoryStr);
            map.putAll(brandAndSpecMap);
        } else {
            //将品牌和规格返回结果中
            if (categoryList.size() > 0) {
                Map<String, Object> brandAndSpecMap = searchBrandAndSpec((String) categoryList.get(0));
                map.putAll(brandAndSpecMap);
            }

        }


        return map;
    }



    //根据关键字搜索查询列表,私有的方法,高亮显示
    private Map<String, Object> searchList(Map keyWordsMap) {
        //2.调用高亮的query接口，注意，不需要调用有参构造
        HighlightQuery query = new SimpleHighlightQuery();
        //4.在高亮选项中添加field以solr索引库中的标题域(高亮的域)
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        //5.为高亮域设置前缀
        highlightOptions.setSimplePostfix("</em>");
        //6.为高亮域设置后缀
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        //3.设置高亮的选项
        query.setHighlightOptions(highlightOptions);
        //---高亮设置end-----
        //--搜索关键字ing----
        Criteria criteria = new Criteria("item_keywords").is(keyWordsMap.get("keyWords"));
        query.addCriteria(criteria);

        //通过筛选过滤条件
        //过滤分类
        if (!"".equals(keyWordsMap.get("category"))) {
            Criteria filtercriteria = new Criteria("item_category").is(keyWordsMap.get("category"));
            FilterQuery filterQuery = new SimpleFilterQuery(filtercriteria);
            query.addFilterQuery(filterQuery);
        }
        //过滤品牌
        if (!"".equals(keyWordsMap.get("brand"))) {
            Criteria filtercriteria = new Criteria("item_brand").is(keyWordsMap.get("brand"));
            FilterQuery filterQuery = new SimpleFilterQuery(filtercriteria);
            query.addFilterQuery(filterQuery);
        }

        //过滤规格
        if (null != keyWordsMap.get("spec")) {
            Map<String, String> map = (Map<String, String>) keyWordsMap.get("spec");
            for (String key : map.keySet()) {
                Criteria filtercriteria = new Criteria("item_spec_" + key).is(map.get(key));
                FilterQuery filterQuery = new SimpleFilterQuery(filtercriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //价格查询
        if (!"".equals(keyWordsMap.get("price"))) {
            String priceStr = (String) keyWordsMap.get("price");
            String[] prices = priceStr.split("-");
            if (!"0".equals(prices[0])) {
                Criteria filtercriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
                FilterQuery filterQuery = new SimpleFilterQuery(filtercriteria);
                query.addFilterQuery(filterQuery);
            }
            if (!"*".equals(prices[1])) {
                Criteria filtercriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                FilterQuery filterQuery = new SimpleFilterQuery(filtercriteria);
                query.addFilterQuery(filterQuery);

            }
        }

        //分页查询
        //System.out.println("111");
        Integer myPageNum = (Integer) keyWordsMap.get("myPageNum");
        Integer myPageSize = (Integer) keyWordsMap.get("myPageSize");
        //System.out.println(myPageNum);
       /* Integer myPageNum = Integer.valueOf((String) keyWordsMap.get("myPageNum")) ;
        System.out.println(myPageNum);
        Integer myPageSize = Integer.valueOf((String) keyWordsMap.get("myPageSize")) ;*/
        if(null==myPageNum){
            myPageNum=1;
        }
        if(null==myPageSize){
            myPageSize=20;
        }
        query.setOffset(myPageNum);
        query.setRows(myPageSize);
        /*System.out.println("页数"+keyWordsMap.get("myPageNum"));
        System.out.println("每页显示记录数"+keyWordsMap.get("myPageSize"));*/

        //排序查询
        String sortValue = (String) keyWordsMap.get("sort");//升序或降序
        String sortFieldValue = (String) keyWordsMap.get("sortField");//相关字段
        if(null!=sortValue&&!"".equals(sortValue)){
           if (sortValue.equals("ASC")){//升序
               Sort sort = new Sort(Sort.Direction.ASC,"item_"+sortFieldValue);
               query.addSort(sort);

           }
           if(sortValue.equals("DESC")){//降序
               Sort sort = new Sort(Sort.Direction.DESC,"item_"+sortFieldValue);
               query.addSort(sort);
           }
        }


        //1.使用solr模板调用高亮查询方法
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //7.获取高亮入口集合
        List<HighlightEntry<TbItem>> entries = page.getHighlighted();
        for (HighlightEntry<TbItem> entry : entries) {
            //获取高亮列表(高亮域的个数)
            List<HighlightEntry.Highlight> highlightList = entry.getHighlights();
            /*
            for(Highlight h:highlightList){
                List<String> sns = h.getSnipplets();//每个域有可能存储多值
                System.out.println(sns);
            }*/
            if (highlightList.size() > 0 && highlightList.get(0).getSnipplets().size() > 0) {
                TbItem item = entry.getEntity();
                item.setTitle(highlightList.get(0).getSnipplets().get(0));
            }
        }
        //创建一个新的map集合
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", page.getContent());
        resultMap.put("totalPage",page.getTotalPages());//总页数
        resultMap.put("total",page.getTotalElements());//总记录数
        return resultMap;
    }


    //从solr索引库中分组查询商品分类集合
    private List findCategoryList(Map keyWordsMap) {
        //2.根据关键字进行查询
        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(keyWordsMap.get("keyWords"));
        query.addCriteria(criteria);
        //3.设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        //1.分组查询，获取分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        //4.获取分组结果集
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        //5.获取分组结果入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        //6.获取分组结果入口集合
        List<GroupEntry<TbItem>> groupList = groupEntries.getContent();
        //7.创建一个新的list集合用来承载商品分类结果
        List list = new ArrayList();
        for (GroupEntry<TbItem> tbItemGroupEntry : groupList) {
            //getGroupValue()表示获取商品分类的值
            list.add(tbItemGroupEntry.getGroupValue());
        }
        return list;


    }


    //返回品牌和规格 从缓存中查询
    private Map<String, Object> searchBrandAndSpec(String category) {
        Map map = new HashMap();
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
        List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
        map.put("brandList", brandList);
        map.put("specList", specList);
        return map;
    }

    //当商品审核时更新索引库
    @Override
    public void saveList(List list) {
        solrTemplate.saveBeans(list);
        System.out.println("方法执行了吗？");
        solrTemplate.commit();
    }

    //当商品删除时更新索引库
    @Override
    public void delGoods(List listGoodIds) {
        //根据goodsid进行删除
        Criteria criteria = new Criteria().and("item_goodsid").in(listGoodIds);
        Query query = new SimpleQuery();
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

}
