package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

public interface SearchItemService {
    public Map<String,Object> search(Map keyWordsMap);
    public void saveList(List list);
    public void delGoods(List listGoodIds);
}
