package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

/**
 * 
 * @author 庞湘耀
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemDao;

	@Autowired
	private TbItemDescMapper itemDescDao;

	@Autowired
	private JmsTemplate jmsTemplate;

	// @Resource根据Id注入， 查找有没有该属性名的bean
	@Resource
	private Destination topicDestination;

	// 缓存
	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;

	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;

	/**
	 * 根据id查找商品
	 */
	public TbItem findItemById(Long id) {
		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + id + ": BASE");
			if (StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缓存中没有查询数据库
		// 根据主键查询
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		List<TbItem> list = itemDao.selectByExample(example);
		if (list.size() > 0 && list != null) {
			TbItem item = new TbItem();
			item = list.get(0);
			// 把结果添加到缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE + ":" + id + ": BASE", JsonUtils.objectToJson(item));
				// 设置过期时间
				jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ": BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return item;
		}
		return null;
	}

	/**
	 * 用于分页查询的获取item结果集
	 */
	public EasyUIResult getItemResult(int page, int rows) {
		PageHelper.startPage(page, rows);
		EasyUIResult result = new EasyUIResult();
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemDao.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	/**
	 * 添加商品
	 */
	public E3Result addItem(TbItem item, String desc) {
		// 生成商品id
		final long ItemId = IDUtils.genItemId();
		// 补全商品属性
		item.setId(ItemId);
		// 1-正常 ，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 向商品插入数据
		itemDao.insert(item);
		// 创建一个商品描述表对应的pojo对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全属性
		itemDesc.setItemId(ItemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 将商品描述表插入数据
		itemDescDao.insert(itemDesc);
		// 发送商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(ItemId + "");
				return textMessage;
			}
		});
		// 返回成功
		return E3Result.ok();
	}

	/**
	 * 根据商品id获取商品描述
	 */
	public TbItemDesc getItemDescById(Long id) {
		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + id + ": DESC");
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc tbItemDesc = itemDescDao.selectByPrimaryKey(id);
		// 把结果添加到缓存
		try {
			jedisClient.set(REDIS_ITEM_PRE + ":" + id + ": DESC", JsonUtils.objectToJson(tbItemDesc));
			// 设置过期时间
			jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ": DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}
}
