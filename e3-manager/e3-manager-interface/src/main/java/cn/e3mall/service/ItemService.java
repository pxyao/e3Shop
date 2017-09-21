package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {
	public TbItem findItemById(Long id);
	public EasyUIResult getItemResult(int page,int rows);
	public E3Result addItem(TbItem item,String desc);
	public TbItemDesc getItemDescById(Long id);
}
