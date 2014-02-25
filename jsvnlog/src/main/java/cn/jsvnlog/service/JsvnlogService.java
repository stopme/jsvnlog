package cn.jsvnlog.service;
import java.util.List;
import java.util.Map;

public interface JsvnlogService {
	public String builderSearchIndex(List<?> allT)throws Exception;
	public String createSearchIndex(Object o)throws Exception;
	public List<?> queryAllSearchIndex(Object o)throws Exception;
	public Object getOneIndexById(Object o) throws Exception;
	String updateSearchIndex(Object t) throws Exception;
	public List<?> getChildRePositoryFromESByJRrepository(Object t) throws Exception;
	public List<?> getAllLogsFromESByParams(Map<String,String> fieldValue,Class<?> t) throws Exception;
}
