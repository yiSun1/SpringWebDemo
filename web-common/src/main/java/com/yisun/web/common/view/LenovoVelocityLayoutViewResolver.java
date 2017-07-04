package com.yisun.web.common.view;

import com.yisun.web.common.url.WebUrl;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import java.util.Map;

/**
 * 直接继承AbstractTemplateViewResolver，
 * 从而禁用了原有属性：layoutUrl，layoutKey，screenContentKey，dateToolAttribute，numberToolAttribute，toolboxConfigLocation
 */
public class LenovoVelocityLayoutViewResolver extends AbstractTemplateViewResolver {

    private Map<String, Object> velocityTools;
    private Map<String, WebUrl> velocityUrl;

    public LenovoVelocityLayoutViewResolver(){
    }

    protected Class<?> requiredViewClass() {
        return LenovoVelocityLayoutView.class;
    }

    public void setVelocityTools(Map<String, Object> velocityTools) {
        this.velocityTools = velocityTools;
    }

    public void setVelocityUrl(Map<String, WebUrl> velocityUrl) {
        this.velocityUrl = velocityUrl;
    }

    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        LenovoVelocityLayoutView view = (LenovoVelocityLayoutView)super.buildView(viewName);
        if(this.velocityTools != null) {
            view.setVelocityTools(this.velocityTools);
        }

        if(this.velocityUrl != null) {
            view.setVelocityUrl(this.velocityUrl);
        }

        return view;
    }
}
