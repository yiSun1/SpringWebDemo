package com.yisun.web.common.url;


/**
* url渲染拦截器
*
* @author <a href="mailto:winstonvip@gmail.com">winston</a>
* @version Created by IntelliJ IDEA.
*          Date: 2010-5-4 18:22:37
* @since 1.0
*/
public interface WebUrlIntercept {

    /**
     * 拦截渲染内容
     * @param jdUrl 需要渲染的内容
     */
    void doIntercept(WebUrl jdUrl);
}
