package com.yisun.web.common.util;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-6-18
 * Time: 上午11:28
 * To change this template use File | Settings | File Templates.
 */
public class JacksonUtil {
    private static ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    /**
     * 对象转json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        try {

            return mapper.writeValueAsString(object);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * json字符串转对象
     *
     * @param json
     * @param type
     * @return
     */
    public static <T> T toObject(String json, Class<T> type) throws IOException {
        if (json == null) {
            return null;
        }
        return mapper.readValue(json, type);

    }

    public static Map<String, String> toMap(String json) throws IOException {
        Map<String, String> mapclass = new HashMap<String, String>();
        return toObject(json, mapclass.getClass());
    }

    public static Map<String, Object> toObjectMap(String json) throws IOException {
        Map<String, Object> mapclass = new HashMap<String, Object>();
        return toObject(json, mapclass.getClass());
    }


    /**
     * json字符串转对象列表
     *
     * @param json 字符串
     * @param type List<Bean>中Bean的类型
     * @return
     */
    public static <T> T toList(String json, Class<T> type) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, getCollectionType(ArrayList.class, type));
        } catch (Exception e) {
        }
        return null;
    }

    public static String instanceofTo(String json) throws IOException {
        Object object = toObject(json, Object.class);
        if (object instanceof List) {
            return "list";
        }
        if (object instanceof Map) {
            return "map";
        }
        return "";
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return java 类型
     */
    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * json字符串转JSONNode
     *
     * @param json
     * @return
     */
    public static JsonNode toJsonNode(String json) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> T toJsonString(JsonNode jsonNode, String key, Class<T> type) {
        return JacksonUtil.toObject(jsonNode.findValue(key), type);
    }

    /**
     * JsonNode转对象
     *
     * @param node json节点
     * @param type 对象类型
     * @return
     */
    public static <T> T toObject(JsonNode node, Class<T> type) {
        if (node == null) {
            return null;
        }
        try {
            return mapper.readValue(node, type);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * JsonNode转对象列表
     *
     * @param node json节点
     * @param type List<Bean>中Bean的类型
     * @return
     */
    public static <T> T toList(JsonNode node, Class<T> type) {
        if (node == null) {
            return null;
        }
        try {
            return mapper.readValue(node, getCollectionType(ArrayList.class, type));
        } catch (Exception e) {
        }
        return null;
    }



    public static void main(String[] arg) throws IOException {
//        Consult c = new Consult();
//        c.response ="error" ;
//        c.error = new ResError();
//        c.error.text = "错了";
//        System.out.println(toJson(c));
        String s = "{\"common\":{\"userId\":\"11111\",\"userSession\":\"ancndnd\",\"mid\":\"0\",\"test1\":\"MD5\"},\"data\":{\"address\":\"12306\",\"couponcard\":[\"100\",\"200\"],\"cartId\":\"10086\",\"payway\":{\"amount\":100.2,\"otherPay\":1},\"expresstime\":\"0_0\"}}";
        String test = "{\"common\":{\"userId\":\"\",\"userSession\":\"\",\"mid\":\"0\",\"test1\":\"\"},\"data\":[{\"count\":\"10\",\"skuId\":\"64858\",\"classType\":\"0\",\"productType\":\"0\"},{\"count\":\"10\",\"skuId\":\"64858\",\"classType\":\"0\",\"productType\":\"0\"}]}";
        String list = "[ {count\":\"10\",\"skuId\":\"64858\",\"classType\":\"0\",\"type\":1,\"productType\":\"0\"},{count\":\"10\",\"skuId\":\"64858\",\"classType\":\"0\",\"type\":1,\"productType\":\"0\"}]";


    }

    class RequestD {
        public String count;
        public String skuId;
        public String classType;

        public String productType;


    }
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz)
            throws Exception {
        List<Map<String, Object>> list = mapper.readValue(jsonArrayStr,
                new TypeReference<List<T>>() {
                });
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(map2pojo(map, clazz));
        }
        return result;
    }
    /**
     * map convert to javaBean
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return mapper.convertValue(map, clazz);
    }
}
