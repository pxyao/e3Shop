package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 内容管理Controller
 * @author 庞湘耀
 *
 */
@Controller
public class ContentServiceController {
	
	@Autowired
	private ContentService contentService;
	
	/**
	 * 插入新内容
	 * @param content
	 * @return
	 */
	@RequestMapping(value="/content/save")
	@ResponseBody
	public E3Result addContent(TbContent content){
		return contentService.addContent(content);
	}
	
	/**
	 * 展示该分类的所有内容
	 * @return
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIResult getContentResult(int page, int rows,Long categoryId){
		return contentService.getContentResult(page, rows,categoryId);
	}
}
