package cn.jsvnlog.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 检索的service最好实现此类
 * @author Administrator
 *
 */
public class JBaseServiceImpl{

	protected Object getObjectId(Object o){
		Class<?> rejectObject =null;
		try{
			rejectObject=Class.forName(o.getClass().getName());
		Method	method = rejectObject.getMethod("getId");
		Object id =  method.invoke(o);
		return id;
        }catch (Exception e) {
            e.printStackTrace();
        }
		return 0;
	}
	protected Map<Object,Object> getObjectByForeignkey(Object o){
		Class<?> rejectObject =null;
		try{
			rejectObject=Class.forName(o.getClass().getName());
		Field[] fields = rejectObject.getDeclaredFields();
		Map<Object,Object> map = new HashMap<Object,Object>();
		for (int i = 0; i < fields.length; i++) {
			Object name = fields[i].getName();
			String nameStr = name.toString();
			String first = nameStr.substring(0, 1);
			String methodName = "get"+first.toUpperCase()+nameStr.substring(1);
			Method	method = rejectObject.getMethod(methodName);
			Object value = method.invoke(o);
			if(null != value){
				map.put(name, value);
			}
		}
		return map;
        }catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
}
