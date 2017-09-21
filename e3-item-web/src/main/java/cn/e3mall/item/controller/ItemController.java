package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

/**
 * 商品详细页面Controller
 * @author 庞湘耀
 *
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable long itemId,Model model){
		//调用服务取商品基本信息
		TbItem tbitem = itemService.findItemById(itemId);
		Item item = new Item(tbitem);
		//取商品描述信息
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
		
		model.addAttribute("item",item);
		model.addAttribute("itemDesc",tbItemDesc);
		//返回逻辑视图
		return "item";
	}
	
}
