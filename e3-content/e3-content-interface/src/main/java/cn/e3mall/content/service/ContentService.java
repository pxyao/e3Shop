package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUIResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	//添加内容
	public E3Result addContent(TbContent content);
	//获取内容分页列表
	public EasyUIResult getContentResult(int page, int rows,Long categoryId);
	//根据contentId查找内容
	public List<TbContent> getContentListByCid(Long cid);
}
