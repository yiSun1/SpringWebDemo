package com.yisun.web.common.view;

import com.yisun.web.common.url.WebUrl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.springframework.web.servlet.view.velocity.VelocityView;

import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by chenww3 on 2015/4/12.
 */
public class LenovoVelocityLayoutView extends VelocityView {

    private static final Log log = LogFactory.getLog(LenovoVelocityLayoutView.class);

    public static String KEY_SCREEN_CONTENT = "screen_content";
    public static String KEY_EXCEPTION = "exception";
    public static String KEY_LAYOUT = "layout";
    public static final String PROPERTY_DEFAULT_LAYOUT = "tools.view.servlet.layout.default.template";
    public static final String PROPERTY_ERROR_TEMPLATE = "tools.view.servlet.error.template";
    public static final String PROPERTY_LAYOUT_DIR = "tools.view.servlet.layout.directory";
    public static final String OUTPUT_ENCODING = "output.encoding";
    public static final String INPUT_ENCODING = "input.encoding";

    protected String defaultLayout;
    protected String errorTemplate;
    protected String layoutDir;
//    protected String inputEncoding;
//    protected String outputEncoding;

    private Map<String, Object> velocityTools;
    private Map<String, WebUrl> velocityUrl;

    public void setVelocityTools(Map<String, Object> velocityTools) {
        this.velocityTools = velocityTools;
    }

    public void setVelocityUrl(Map<String, WebUrl> velocityUrl) {
        this.velocityUrl = velocityUrl;
    }

    /**
     * 将tools、url中的内容合并到上下文中
     * 先合url，再合tools，如果里面已经存在，则不变
     *
     * @param context
     */
    private void mergeContext(Context context) {
        mergeUrl(context, velocityUrl);
        mergeTool(context, velocityTools);
    }

    private void mergeTool(Context context, Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> stringObjectEntry : map.entrySet()) {
                context.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
        }
    }

    private void mergeUrl(Context context, Map<String, WebUrl> map) {
        if (map != null) {
            for (Map.Entry<String, WebUrl> stringWebUrlEntry : map.entrySet()) {
                String key = stringWebUrlEntry.getKey();
                WebUrl org = stringWebUrlEntry.getValue();
                WebUrl value = org.clone();//解决多线程并发的问题。
                value.setWebUrl(org); //原始的不拿出来配置。
                context.put(key, value);
            }
        }
    }

    protected void doRender(Context context, HttpServletResponse response) throws Exception {
        try {
            String finalLocation = this.getUrl();
            doIt(finalLocation, context, response, null);
        } catch (Exception e) {
            //渲染出错后，跳到出错页面的再次渲染
            doIt(errorTemplate, context, response, e);
        }
    }

    private void doIt(String finalLocation, Context context, HttpServletResponse response, Exception exception) throws Exception {
        mergeContext(context);
        if (exception != null) {
            context.put(KEY_EXCEPTION, exception);
        }
        this.renderScreenContent(finalLocation, context);

        this.renderLayoutContent(context, response);
    }

    private void renderLayoutContent(Context velocityContext, HttpServletResponse response) throws Exception {
        Object obj = velocityContext.get(KEY_LAYOUT);
        String layout = (obj == null) ? null : obj.toString();
        if (layout == null) {
            layout = defaultLayout;
        } else {
            layout = layoutDir + layout;
        }

        Template layoutVm = null;
        try {
            layoutVm = getTemplate(layout);
        } catch (Exception e) {
            log.error("LenovoVelocityLayoutView: Can't load layout \"" + layout + "\": " + e);
            if (!layout.equals(defaultLayout)) {
                layoutVm = getTemplate(defaultLayout);
            }
        }
        this.mergeTemplate(layoutVm, velocityContext, response);
    }


    private void renderScreenContent(String finalLocation, Context velocityContext) throws Exception {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Rendering screen content template [" + finalLocation + "]");
        }

        StringWriter sw = new StringWriter();
//        Template screenContentTemplate = this.getTemplate(this.getUrl());
        Template screenContentTemplate = this.getTemplate(finalLocation);
        screenContentTemplate.merge(velocityContext, sw);
//        velocityContext.put(this.screenContentKey, sw.toString());
        velocityContext.put(KEY_SCREEN_CONTENT, sw.toString());
    }

    @Override
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        if (velocityEngine != null && getVelocityEngine() == null) {
//            outputEncoding = (String) velocityEngine.getProperty(OUTPUT_ENCODING);
//            inputEncoding = (String) velocityEngine.getProperty(INPUT_ENCODING);

            defaultLayout = (String) velocityEngine.getProperty(PROPERTY_DEFAULT_LAYOUT);
            layoutDir = (String) velocityEngine.getProperty(PROPERTY_LAYOUT_DIR);
            if (!layoutDir.endsWith("/")) {
                layoutDir += '/';
            }
            if (!layoutDir.startsWith("/")) {
                layoutDir = "/" + layoutDir;
            }
            // for efficiency's sake, make defaultLayout a full path now
            defaultLayout = layoutDir + defaultLayout;
            errorTemplate = (String) velocityEngine.getProperty(PROPERTY_ERROR_TEMPLATE);
            if (log.isDebugEnabled()) {
                log.debug("defaultLayout=[" + defaultLayout + "], layoutDir=[" + layoutDir + "], errorTemplate=[" + errorTemplate + "]");
            }
            super.setVelocityEngine(velocityEngine);
        }
    }


}
