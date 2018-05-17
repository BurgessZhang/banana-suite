package com.burgess.banana.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.spring
 * @file BananaSpringInstanceProvider.java
 * @time 2018-05-17 15:09
 * @desc
 */
public class BananaSpringInstanceProvider {

    private ApplicationContext applicationContext;

    /**
     * @param '[locations spring配置文件的路径集合，spring将会从类路径开始获取这批资源文件]
     * @return
     * @class_name BananaSpringInstanceProvider
     * @method BananaSpringInstanceProvider
     * @desc 以一批spring配置文件的路径初始化spring实例提供者
     * @author free.zhang
     * @date 2018/5/17 15:09
     */
    public BananaSpringInstanceProvider(String... locations) {
        applicationContext = new ClassPathXmlApplicationContext(locations);
    }

    /**
     * @param '[applicationContext]
     * @return
     * @class_name BananaSpringInstanceProvider
     * @method BananaSpringInstanceProvider
     * @desc 从ApplicationContext生成SpringProvider
     * @author free.zhang
     * @date 2018/5/17 15:11
     */
    public BananaSpringInstanceProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * @param '[annotatedClasses]
     * @return
     * @class_name BananaSpringInstanceProvider
     * @method BananaSpringInstanceProvider
     * @desc 根据一批spring配置文件初始化spring实例提供者
     * @author free.zhang
     * @date 2018/5/17 15:13
     */
    public BananaSpringInstanceProvider(Class<?>... annotatedClasses) {
        applicationContext = new AnnotationConfigApplicationContext(annotatedClasses);
    }

    /**
     * @param '[beanClass 实例的类型]
     * @return T 指定类型的实例
     * @class_name BananaSpringInstanceProvider
     * @method getInstance
     * @desc 返回指定类型的实例
     * @author free.zhang
     * @date 2018/5/17 15:14
     */
    public <T> T getInstance(Class<T> beanClass) {
        String[] beanNames = applicationContext.getBeanNamesForType(beanClass);
        if (0 == beanNames.length) {
            return null;
        }
        return (T) applicationContext.getBean(beanNames[0]);
    }

    public <T> T getInstance(Class<T> beanClass, String beanName) {
        return (T) applicationContext.getBean(beanName, beanClass);
    }

    @SuppressWarnings("unchecked")
    public <T> T getByBeanName(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    public <T> int getInterfaceCount(Class<T> beanClass) {
        return applicationContext.getBeanNamesForType(beanClass).length;
    }

    public <T> Map<String, T> getInterfaces(Class<T> beanClass) {
        return applicationContext.getBeansOfType(beanClass);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
