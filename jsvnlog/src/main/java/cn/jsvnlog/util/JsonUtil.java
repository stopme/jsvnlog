package cn.jsvnlog.util;

import java.io.StringWriter;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {
    public static String object2Json(Object obj) {

        try {

            StringWriter sw = new StringWriter();
            JsonGenerator gen = new JsonFactory().createJsonGenerator(sw);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(gen, obj);
            gen.close();

            return sw.toString();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

     }

}
