package com.burgess.banana.spring.web;

import java.util.Map;

import javax.servlet.ServletContextEvent;

import com.burgess.banana.spring.BananaApplicationStartedListener;
import com.burgess.banana.spring.BananaInstanceFactory;
import com.burgess.banana.spring.BananaSpringInstanceProvider;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.spring.web
 * @file BananaContextLoaderListener.java
 * @time 2018-05-17 15:26
 * @desc
 */
public class BananaContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        String serviceName = event.getServletContext().getInitParameter("appName");
        System.setProperty("serviceName", serviceName == null ? "undefined" : serviceName);
        super.contextInitialized(event);
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
        BananaSpringInstanceProvider provider = new BananaSpringInstanceProvider(applicationContext);
        BananaInstanceFactory.loadFinished(provider);

        Map<String, BananaApplicationStartedListener> interfaces = applicationContext.getBeansOfType(BananaApplicationStartedListener.class);
        if(interfaces != null){
            for (BananaApplicationStartedListener listener : interfaces.values()) {
                System.out.println(">>>begin to execute listener:"+listener.getClass().getName());
                listener.onApplicationStarted(applicationContext);
                System.out.println("<<<<finish execute listener:"+listener.getClass().getName());
            }
        }
    }
}
