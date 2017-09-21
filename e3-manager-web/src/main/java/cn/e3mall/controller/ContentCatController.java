package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;

/**
 * 内容分类管理
 * @author 庞湘耀
 *
 */
@Controller
public class ContentCatController {
	
	@Autowired
	private ContentCategoryService categoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(
			@RequestParam(name="id",defaultValue="0") 
			Long parentId){
		List<EasyUITreeNode> list = categoryService.getContentCatList(parentId);
		return list;
	}
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result addContentCategoryCat(Long parentId,String name){
		E3Result result = categoryService.addContentCategory(parentId, name);
		return result;
		
	}
	
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public E3Result removeContentCategoryCat(Long id){
		E3Result result = categoryService.removeContentCategory(id);
		return result;
	}

	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result updateConetntCategoryCat(Long id,String name){
		E3Result result = categoryService.updateContentCategoryName(id, name);
		return result;
	}
}
