package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

/**
 * 商品搜索Dao
 * 
 * @author 庞湘耀
 *
 */
@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;

	/**
	 * 根据查询条件查询索引库
	 * 
	 * @param query
	 * @return
	 * @throws SolrServerException 
	 */
	public SearchResult search(SolrQuery query) throws Exception {
		SearchResult searchResult = new SearchResult();

		// 根据query查询索引库
		QueryResponse queryResponse = solrServer.query(query);
		// 取查询结果
		SolrDocumentList results = queryResponse.getResults();
		// 取查询结果总记录数
		long numFound = results.getNumFound();
		searchResult.setRecordCount(numFound);
		// 取商品列表，需要取高亮显示
		Map<String, Map<String, List<String>>> hl = queryResponse.getHighlighting();
		List<SearchItem> list = new ArrayList<>();
		for (SolrDocument solrDocument : results) {
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));

			// 取高亮显示
			List<String> hlist = hl.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if (hlist.size() >= 0 && hlist != null) {
				title = hlist.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle((title));

			list.add(searchItem);
		}
		searchResult.setItemList(list);
		// 返回结果
		return searchResult;
	}
}
