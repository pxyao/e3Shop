package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理Service
 * @author 庞湘耀
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper categoryMapper;
	
	/**
	 * 获取内容分类列表
	 */
	public List<EasyUITreeNode> getContentCatList(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//根据父节点来查询该父节点的所有子节点
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = categoryMapper.selectByExample(example);
		List<EasyUITreeNode> easyUITreeNodes = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			//将每个查询到的节点插入到实体类中
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			easyUITreeNodes.add(node);
		}
		return easyUITreeNodes;
	}

	//添加分类
	public E3Result addContentCategory(long parentId, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		//封装POJO
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//可选值:1(正常),2(删除)',
		contentCategory.setStatus(1);
		//新添加的节点一定是叶子节点
		contentCategory.setIsParent(false);
		//表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数',
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//设置父节点为父节点
		TbContentCategory parentCategory = categoryMapper.selectByPrimaryKey(parentId);
		if (parentCategory.getIsParent() != true) {
			parentCategory.setIsParent(true);
			categoryMapper.updateByPrimaryKey(parentCategory);
		}
		categoryMapper.insert(contentCategory);
		return E3Result.ok(contentCategory);
	}
	
	/**
	 * 删除分类 不能删除有子节点的分类
	 * @return
	 */
	public E3Result removeContentCategory(Long id){
		//查询是否有该记录
		TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
		if (contentCategory == null) {
			return E3Result.build(502, "数据库中没有该id的记录");
		}
		//查询该节点是不是叶子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//如果该记录有子节点，那么就有人的parentId为该记录的id
		criteria.andParentIdEqualTo(id);
		List<TbContentCategory> parentCategory = categoryMapper.selectByExample(example);
		if (parentCategory.size() != 0) {
			//如果该节点不是叶子节点,返回501
			return E3Result.build(501, "该节点不是叶子节点,不能删除");
		}
		//删除该分类
		categoryMapper.deleteByPrimaryKey(id);
		return E3Result.ok();
	}
	
	/**
	 * 修改分类名称
	 */
	public E3Result updateContentCategoryName(Long id,String name){
		TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
		if (contentCategory != null && !contentCategory.getName().equals(name)) {
			//如何有这个分类，并且和传过来名字不一样。 就修改它的名称
			contentCategory.setName(name);
			categoryMapper.updateByPrimaryKey(contentCategory);
		}
		return E3Result.ok();
	}
}
