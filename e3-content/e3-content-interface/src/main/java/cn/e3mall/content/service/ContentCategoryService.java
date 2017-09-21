package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;

public interface ContentCategoryService {
	//获得分类列表
	public List<EasyUITreeNode> getContentCatList(Long parentId);
	//添加分类
	public E3Result addContentCategory(long parentId,String name);
	//删除分类
	public E3Result removeContentCategory(Long id);
	//修改分类名称
	public E3Result updateContentCategoryName(Long id,String name);
}
