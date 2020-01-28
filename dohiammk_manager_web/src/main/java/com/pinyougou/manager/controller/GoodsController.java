package com.pinyougou.manager.controller;
import java.util.Arrays;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;
import entity.PageResult;
import entity.Result;
import javax.jms.*;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	/*@Reference
	private SearchItemService searchItemService;*/

	@Reference
	private ItemPageService itemPageService;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination queueSolrDestination;//审核通过后添加索引库

	@Autowired
	private Destination queueSolrDelDestination;//根据goodsId删除
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}

	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findMyPage")
	public PageResult  findMyPage(@RequestBody TbGoods goods,int page,int rows){
		//System.out.println("加进来了吗？");
		return goodsService.findMyPage(goods,page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbGoods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbGoods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(final Long [] ids){
		try {
			goodsService.delete(ids);
			jmsTemplate.send(queueSolrDelDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {

					return session.createObjectMessage(ids);
				}
			});
			//searchItemService.delGoods(Arrays.asList(ids));
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}

	@RequestMapping("/auditOrRejectShop")
	public Result auditOrRejectShop(Long[] ids,String status){
		try {
			goodsService.commitShop(ids,status);
			System.out.println("状态："+status);
			if("2".equals(status)){
				final List<TbItem> tbItemList = goodsService.findItemByGoodIdAndStatus(ids, status);
                System.out.println("集合的长度"+tbItemList.size());
				final String toJSONString = JSON.toJSONString(tbItemList);
				if(tbItemList.size()>0){
					//通过消息中间件将修改的数据传递
					jmsTemplate.send(queueSolrDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							TextMessage message = session.createTextMessage(toJSONString);
							System.out.println("消息传递过去啦");
							return message;
						}
					});
					//searchItemService.saveList(tbItemList);
				}

			}
			return new Result(true, "审核或者驳回成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "审核或者驳回修改失败");
		}
	}

	//获取静态页面
	@RequestMapping("/getHtml")
	public void getHtml(Long goodsId){
		itemPageService.getHtml(goodsId);
	}
	
}
