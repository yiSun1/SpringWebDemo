package com.yisun.web.common.url;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 地址生成器。<br>
 * 保证，多次调用后，tostring后，属性值还原<br>
 * 还要保证并发不会出问题<br>
 *
 * @author <a href="mailto:winstonvip@gmail.com">winston</a>
 * @version Created by IntelliJ IDEA.
 *          Date: 2010-5-4 18:22:37
 * @since 1.0
 */
public class WebUrl implements Cloneable {
    private final static Log log = LogFactory.getLog(WebUrl.class);
    /**
     * 认证的用户名
     */
    private String username;
    /**
     * 认证的密码
     */
    private String password;
    /**
     * 协议
     */
    private String protocol = "http";
    /**
     * 域名
     */
    private String host;
    /**
     * 端口
     */
    private int port = -1;

    /**
     * 路径
     */
    private String path;
    /**
     * 上下文路径
     */
    private String contextPath;

    private boolean reset;
    /**
     * 是不是过滤null和空串
     */
    private boolean filter = true;
    /**
     * 查询参数
     */
    private Map<String, Object> query = new LinkedHashMap<String, Object>();

    /**
     * 保存一份
     */
    private WebUrl webUrl;

    /**
     * 自定义渲染器
     */
    private WebUrlIntercept intercept;

    private String charsetName = "utf-8";


    public WebUrl() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public void setWebUrl(WebUrl webUrl) {
        this.webUrl = webUrl;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setIntercept(WebUrlIntercept intercept) {
        this.intercept = intercept;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }

    public Map<String, Object> getQuery() {
        return query;
    }

    public WebUrl addQueryData(String name, String value) {
        query.put(name, value);
        return this;
    }

    /**
     * 支持list。list会生成多个参数 <br/>
     * 支持 addQueryData(key,Object[])，这种情况下，key是同一个，value会生成多个<br/>
     * 支持addQueryData(key,Map)，这种情况下，指定的key无效。key采用map中的。<br/>
     * addQueryData(key,List)，这种情况下，key是同一个，value会生成多个、在vm中可以这样：.addQueryData(key,[value1,value2])<br/>
     *
     * @param name
     * @param value
     * @return
     */
    public WebUrl addQueryData(String name, Object value) {
        query.put(name, value);
        return this;
    }

    public WebUrl addQueryData(String name, int value) {
        query.put(name, value);
        return this;
    }

    public WebUrl addQueryData(String name, long value) {
        query.put(name, value);
        return this;
    }

    public WebUrl addQueryData(String name, float value) {
        query.put(name, value);
        return this;
    }

    public WebUrl getTarget(String target) {
        this.path = target;
        return this;
    }

    /**
     * 渲染url。不会复位url的值。
     * 支持List Map 数据和普通对象
     *
     * @return
     */
    public String render() {
        WebUrl url = null;
        try {
            if (intercept != null) {
                url = new WebUrl();
                url.query = new LinkedHashMap<String, Object>();
                if(this.query!=null){
                    url.query.putAll(this.query);
                }

                setWebUrlValue(url, this);
            }
            return doIt();
        } finally {
            if (intercept != null && url != null) {
                setWebUrlValue(this, url);
                if(url.query!=null){
                    this.query.putAll(url.query);
                }
            }
        }
    }

    private String doIt() {
        String str;
        URL url;
        try {
            if (intercept != null) {
                intercept.doIntercept(this);
            }
            //通过url来构造，
            String path = prefixPath(this.contextPath, this.path);
            //path = this.path == null ? "/" : this.path;
            url = new URL(protocol, host, port, path);
            if (url.getDefaultPort() == url.getPort()) {
                url = new URL(protocol, host, -1, path);
            }
            str = url.toString();
        } catch (Exception e) {
            str = "/";
        }

        StringBuilder builder = new StringBuilder(str);
        if (!query.isEmpty()) {
            for (String key : query.keySet()) {
                Object obj = query.get(key);
                if (obj instanceof List) {
                    List list = (List) obj;
                    for (Object o : list) {
                        setValue(builder, key, o);
                    }
                } else if (obj instanceof Map) {
                    Map map = (Map) obj;
                    for (Object o : map.keySet()) {
                        setValue(builder, o == null ? "" : o.toString(), map.get(o));
                    }
                } else {
                    if (obj != null && obj.getClass().isArray()) {
                        Object[] arrays = (Object[]) obj;
                        for (Object o : arrays) {
                            setValue(builder, key, o);
                        }
                    } else {
                        setValue(builder, key, obj);
                    }
                }
            }
            return builder.replace(str.length(), str.length() + 1, "?").toString();
        } else {
            return str;
        }
//        return builder.toString();
    }

    /**
     * 构造url前缀
     *
     * @param contextPath
     * @param path
     * @return
     */
    public String prefixPath(String contextPath, String path) {
        String returnPath;
        if (path == null || contextPath == null) {
            if (path == null && contextPath == null) {
                returnPath = "/";
            } else if (contextPath == null) {
                returnPath = path;
            } else {
                returnPath = contextPath;
            }
        } else {
            if (contextPath.endsWith("/") && path.startsWith("/")) {
                returnPath = contextPath + path.substring(1);
            } else {
                returnPath = contextPath + path;
            }
        }
        return returnPath;
    }

    public void setValue(StringBuilder builder, String key, Object o) {
        String value = o == null ? "" : o.toString();
        if (value.length() > 0) {
            String str1;
            str1 = encodeUrl(value);
            builder.append("&").append(key).append("=").append(str1);
        } else {
            if (!filter) {
                builder.append("&").append(key).append("=");
            }
        }
    }

    /**
     * 按照当前的编码方式来编码url
     *
     * @param value 指定的url
     * @return 编码后。如果肯定编码方式不存在，则返回当前值
     */
    public String encodeUrl(String value) {
        String str1;
        if (StringUtils.isNotBlank(charsetName)) {
            try {
                str1 = URLEncoder.encode(value, charsetName);
            } catch (UnsupportedEncodingException e) {
                str1 = value;
            }
        } else {
            str1 = URLEncoder.encode(value);
        }
        return str1;
    }

    /**
     * 渲染url。会复位url里面的值到初始状态
     *
     * @return
     */
    public String toString() {
        String s = render();
        if (!reset) {//避免copy properties时进入死循环
            reset = true;
            reset();
        }
        reset = false;
        return s;
    }

    /**
     * 复位
     */
    public void reset() {
        try {
            reset = true;
            query.clear();

            if(webUrl!=null) {
                if(webUrl.query!=null){
                    query.putAll(webUrl.query);
                }
                webUrl.setWebUrlValue(this, webUrl);
            }

        } catch (Exception e) {
            log.error("copyProperties error!", e);
        }
    }

    /**
     * 此url后面的path，会加到contextpath中。参数也会加入query map中。
     *
     * @param url
     * @throws java.net.MalformedURLException
     */
    public void setUrl(String url) throws MalformedURLException {
        URL a = new URL(url);
        this.protocol = a.getProtocol();
        this.host = a.getHost();
        this.port = a.getPort();
        this.contextPath = a.getPath();
        String queryString = a.getQuery();
        if (!StringUtils.isEmpty(queryString)) {
            query.putAll(getQueryMap(queryString));
        }
    }

    private Map<String, Object> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (String param : params) {
            String[] strings = param.split("=");
            String name = strings[0];
            String value = null;
            if (strings.length > 1) {
                value = strings[1];
            }
            map.put(name, value);
        }
        return map;
    }

    public WebUrl clone() {
        WebUrl clone = new WebUrl();
        setWebUrlValue(clone, this);
        clone.query = new LinkedHashMap<String, Object>();
        clone.query.putAll(query);
        return clone;
    }

    private void setWebUrlValue(WebUrl dest, WebUrl src) {
        dest.username = src.username;
        dest.password = src.password;
        dest.protocol = src.protocol;
        dest.host = src.host;
        dest.port = src.port;
        dest.contextPath = src.contextPath;
        dest.path = src.path;
        dest.intercept = src.intercept;

    }

    public void cleanQueryMap() {
        if (this.query != null && !this.query.isEmpty()) {
            this.query.clear();
        }
    }


}
