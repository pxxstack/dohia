package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Value("${pageDir}")
    private String pageDir;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private TbGoodsMapper tbGoodsMapper;

    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Override
    public boolean getHtml(Long goodsId) {
        try {
            //获取配置文件
            Configuration configuration = freeMarkerConfig.getConfiguration();
            //获取模板
            Template template = configuration.getTemplate("item.ftl");
            Map dataMap = new HashMap();
            //获取商品信息1
            TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(goodsId);
            //获取商品信息2
            TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(goodsId);
            //获取分类名称
            String category1Name = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName();
            String category2Name  = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName();
            String category3Name  = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();
            //将数据封装到dataMap集合中
            dataMap.put("goods", tbGoods);
            dataMap.put("goodsDesc", tbGoodsDesc);
            dataMap.put("category1Name",category1Name);
            dataMap.put("category2Name",category2Name);
            dataMap.put("category3Name",category3Name);
            //获取sku
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(goodsId);
            criteria.andStatusEqualTo("1");
            example.setOrderByClause("is_default desc");
            List<TbItem> itemList = tbItemMapper.selectByExample(example);
            //封装数据
            dataMap.put("itemList",itemList);
            //创建一个输出流并指定编码 然后将dataMap传输
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream
                            (pageDir + goodsId + ".html"), "UTF-8"
            ));
            template.process(dataMap,out);
            //关闭输出流
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
