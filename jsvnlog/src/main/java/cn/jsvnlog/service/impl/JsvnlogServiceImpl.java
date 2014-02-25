package cn.jsvnlog.service.impl;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.search.sort.Sort;
import io.searchbox.core.search.sort.Sort.Sorting;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.IndicesExists.Builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jsvnlog.configuration.SpringConfiguration;
import cn.jsvnlog.service.JsvnlogService;
import cn.jsvnlog.vo.JRepositoryVO;
import cn.jsvnlog.vo.JSvnLogEntryVO;

@Service("jsvnlogService")
public class JsvnlogServiceImpl extends JBaseServiceImpl implements JsvnlogService {
	@Autowired
	private JestClient jestClient;
	@Override
	public String builderSearchIndex(List<?> allT) throws Exception {
		// TODO Auto-generated method stub
//		IndicesExistsAction indicesExistsRequest  = new IndicesExistsAction();
//		jestClient.execute(indicesExistsRequest);
		return null;
	}
	public static void main(String[] args) throws Exception {
//		Project project1 = new Project();
//		project1.setId(1+"");
//		System.out.println(project1.getClass().getSimpleName().toLowerCase());
//		JsvnlogServiceImpl ss = new JsvnlogServiceImpl();
//		System.out.println(ss.queryAllSearchIndex(project1).size());;
//		System.out.println(id);
		SpringConfiguration con = new SpringConfiguration();
		JestClient client  = con.jestClient();
//		
//		JRepositoryVO p = new JRepositoryVO();
		String id = "C197962302397BAF3A4CC36463DCE5EA";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("repositoryId", id);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		Set<String> setOfParams = params.keySet();
		for (String key : setOfParams) {
			String name = key.toString();
			String value  = params.get(key);
			boolQueryBuilder.must(QueryBuilders.fieldQuery(name, value));
		}
		searchSourceBuilder.query(boolQueryBuilder);
		Search.Builder searchBuilder = new Search.Builder(searchSourceBuilder.toString());
		String index = JSvnLogEntryVO.class.getName().toLowerCase();
		String type = JSvnLogEntryVO.class.getSimpleName().toLowerCase();
		Search searchAction = searchBuilder.addIndex(index)
										   .addType(type)
										   .build();
		JestResult result = client.execute(searchAction);
		System.out.println(result.getSourceAsObjectList(JSvnLogEntryVO.class).size()); ;
		
		
		
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		searchSourceBuilder.query(QueryBuilders.fieldQuery("repositoryId", id));
//		Search.Builder searchBuilder = new Search.Builder(searchSourceBuilder.toString());
//		String index = t.getClass().getName().toLowerCase();
//		String type = t.getClass().getSimpleName().toLowerCase();
//		Search searchAction = searchBuilder.addIndex(index)
//										   .addType(type)
//										   .build();
//		JestResult result = client.execute(searchAction);
		
		
//		
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		searchSourceBuilder.query(QueryBuilders.fieldQuery("parentId", id));
//		Search.Builder searchBuilder = new Search.Builder(searchSourceBuilder.toString());
//		String index = p.getClass().getName().toLowerCase();
//		String type = p.getClass().getSimpleName().toLowerCase();
//		Search searchAction = searchBuilder.addIndex(index)
//										   .addType(type)
//										   .build();
//		JestResult result = client.execute(searchAction);
//		 System.out.println(result.getSourceAsObjectList(p.getClass()).size());
		
		
		
		
		
		
//		Builder builder = new IndicesExists.Builder().addIndex("sss");
//		JestResult result  =client.execute(new IndicesExists(builder));
//		System.out.println( result.isSucceeded());;
//		JRepository r = new JRepository();
//		r.setId("1111111111111");
//		r.setFileName("file name");
//		JsvnlogServiceImpl<Project> s = new JsvnlogServiceImpl<Project>();
//		List<Project> lis = new ArrayList<Project>();
//		for (int i = 0; i < 5; i++) {
//			Project project = new Project();
//			project.setId(i);
//			project.setCreateDate(new Date());
//			project.setName("李枝  " +i);
//			project.setjRepository(r);
//			lis.add(project);
//		}
//		s.create(lis);
	}

	public <T> String create(List<?> allT) throws Exception{
		Object t = allT.get(0) ;
		SpringConfiguration con = new SpringConfiguration();
		JestClient client  = con.jestClient();
		long start = System.currentTimeMillis();
		try {
			// 如果索引存在,删除索引
			System.out.println(t.getClass().getName());
			DeleteIndex.Builder builder1 = new DeleteIndex.Builder(t.getClass().getName().toLowerCase());
			DeleteIndex deleteIndex = builder1.build();
			client.execute(deleteIndex);
			// 创建索引
			CreateIndex.Builder builder2 = new CreateIndex.Builder(t.getClass().getName().toLowerCase());
			CreateIndex createIndex = builder2.build();
			client.execute(createIndex);
			Bulk.Builder builder3 = new Bulk.Builder();
			for (int i = 0; i < allT.size(); i++) {
				builder3.addAction(new Index.Builder(allT.get(i)).build());
			}
			String dafaultType = t.getClass().getSimpleName().toLowerCase();
			String indexName = t.getClass().getName().toLowerCase();
			Bulk bulk = new Bulk(builder3.defaultIndex(indexName).defaultType(dafaultType));
			JestResult result =	client.execute(bulk);
			System.out.println(result.getJsonString());
			return "索引创建成功！";
		} catch (Exception e) {
			e.printStackTrace();
			return "索引创建失败！" + e.getMessage();
		}
	
	}
	@Override
	public String createSearchIndex(Object t) throws Exception {
		// TODO Auto-generated method stub
		Builder builderExists = new IndicesExists.Builder();
		builderExists.addIndex(t.getClass().getName().toLowerCase());
		JestResult result  =jestClient.execute(builderExists.build());
		if(!result.isSucceeded()){
			CreateIndex.Builder builderCreateIndex = new CreateIndex.Builder(t.getClass().getName().toLowerCase());
			CreateIndex createIndex = builderCreateIndex.build();
			jestClient.execute(createIndex);
		}
		Index.Builder builderIndex = new Index.Builder(t);
		String index = t.getClass().getName().toLowerCase();
		builderIndex.index(index);
		String id = getObjectId(t).toString();
		builderIndex.id(id);
		String type = t.getClass().getSimpleName().toLowerCase();
		builderIndex.type(type);
		builderIndex.refresh(true);
		result = jestClient.execute(builderIndex.build());
		return	result.getJsonString();
	}
	@Override
	public String updateSearchIndex(Object t) throws Exception {
		// TODO Auto-generated method stub
		return	createSearchIndex( t);
	}
	
	/**
	 * 这种方法也可以获取数据
		 String query ="{\"query\":{\"match_all\":{}}}";
		 Search.Builder searchBuilder =  new Search.Builder(query);
		 String index = t.getClass().getName().toLowerCase();
		 String type = t.getClass().getSimpleName().toLowerCase();
		 searchBuilder.addIndex(index)
		              .addType(type);
		 Search search = searchBuilder.build();
		 SpringConfiguration con = new SpringConfiguration();
			JestClient client  = con.jestClient();
		 JestResult result = client.execute(search);
	 */
	@Override
	public List<?> queryAllSearchIndex(Object t) throws Exception {
		 SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		 String index = t.getClass().getName().toLowerCase();
		 String type = t.getClass().getSimpleName().toLowerCase();
		 Search search = (Search) new Search.Builder(searchSourceBuilder.toString())
		                              .addIndex(index)
                                      .addType(type)
                                      .build();
		 JestResult result = jestClient.execute(search);
		return result.getSourceAsObjectList(t.getClass());
	}
	@Override
	public Object getOneIndexById(Object o) throws Exception {
		// TODO Auto-generated method stub
		String id = getObjectId(o).toString();
		Get.Builder getActionBuilder = new Get.Builder(id);
		getActionBuilder.id(id);
		String index = o.getClass().getName().toLowerCase();
		getActionBuilder.index(index);
		String type = o.getClass().getSimpleName().toLowerCase();
		getActionBuilder.type(type);
		Get agetAction = getActionBuilder.build();
		JestResult result = jestClient.execute(agetAction);
		return result.getSourceAsObject(o.getClass());
	}
	@Override
	public List<?> getChildRePositoryFromESByJRrepository(Object t) throws Exception {
		// TODO Auto-generated method stub
		String parentId = getObjectId(t).toString();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.size(1000);
		searchSourceBuilder.query(QueryBuilders.fieldQuery("parentId", parentId));
		Search.Builder searchBuilder = new Search.Builder(searchSourceBuilder.toString());
		String index = t.getClass().getName().toLowerCase();
		String type = t.getClass().getSimpleName().toLowerCase();
		Search searchAction = searchBuilder.addIndex(index)
										          .addType(type)
										          .build();
		JestResult result = jestClient.execute(searchAction);
		
		return result.getSourceAsObjectList(t.getClass());
	}
	@Override
	public List<?> getAllLogsFromESByParams(Map<String,String> fieldValue,Class<?> t) throws Exception {
		// TODO Auto-generated method stub
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		Set<String> setOfParams = fieldValue.keySet();
		for (String key : setOfParams) {
			String name = key.toString();
			String value  = fieldValue.get(key);
			boolQueryBuilder.must(QueryBuilders.fieldQuery(name, value));
		}
		searchSourceBuilder.query(boolQueryBuilder);
		Search.Builder searchBuilder = new Search.Builder(searchSourceBuilder.toString());
		String index = t.getName().toLowerCase();
		String type = t.getSimpleName().toLowerCase();
		Sort sort = new Sort("revision", Sorting.ASC);
		Search searchAction = searchBuilder.addSort(sort)
											.addIndex(index)
										    .addType(type)
										    .build();
		JestResult result = jestClient.execute(searchAction);
		return result.getSourceAsObjectList(t);
	}
	
}
