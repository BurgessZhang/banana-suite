package com.burgess.banana.spring.helper;

import java.lang.reflect.Field;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.ClassUtils;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.spring.helper
 * @file BananaSpringAopHelper.java
 * @time 2018-05-17 15:28
 * @desc
 */
public class BananaSpringAopHelper {

    /**
     * @param '[proxy 代理对象]
     * @return java.lang.Object
     * @class_name BananaSpringAopHelper
     * @method getTarget
     * @desc 获取 目标对象
     * @author free.zhang
     * @date 2018/5/17 15:29
     */
    public static Object getTarget(Object proxy) throws Exception {

        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;//不是代理对象
        }

        if (AopUtils.isJdkDynamicProxy(proxy)) {
            Object object = getJdkDynamicProxyTargetObject(proxy);
            return getTarget(object);
        } else { //cglib
            Object object = getCglibProxyTargetObject(proxy);
            return getTarget(object);
        }


    }


    public Class<?> getTargetClass(Object object) {
        return ClassUtils.getUserClass(object);
    }


    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

        return target;
    }


    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();

        return target;
    }
}
