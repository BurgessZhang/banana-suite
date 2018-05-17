package com.burgess.banana.spring;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;


/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.spring
 * @file BananaInstanceFactory.java
 * @time 2018-05-17 15:16
 * @desc 实例工厂类。通过它可以获得其管理的类的实例。 InstanceFactory向客户代码隐藏了IoC工厂的具体实现。在后台，它通过
 *  InstanceProvider策略接口，允许选择不同的IoC工厂，例如Spring， Google Guice和TapestryIoC等等。
 *  IoC工厂应该在应用程序启动时装配好，也就是把初始化好的InstanceProvider
 *  实现类提供给InstanceFactory。对于web应用来说，最佳的初始化方式是创
 *  建一个Servlet过滤器或监听器，并部署到web.xml里面；对普通java应用程
 *  序来说，最佳的初始化位置是在main()函数里面；对于单元测试，最佳的初始 化位置是setUp()方法内部。
 */
public class BananaInstanceFactory {

    private static BananaSpringInstanceProvider instanceProvider;
    private static Long timeStarting = System.currentTimeMillis();
    private static AtomicBoolean initialized = new AtomicBoolean(false);
    private static AtomicBoolean loadFinished = new AtomicBoolean(false);

    /**
     * @param '[provider 一个实例提供者的实例。]
     * @return void
     * @class_name BananaInstanceFactory
     * @method setInstanceProvider
     * @desc 设置实例提供者。
     * @author free.zhang
     * @date 2018/5/17 15:23
     */
    public static void setInstanceProvider(BananaSpringInstanceProvider provider) {
        if (instanceProvider != null) {
            return;
        }
        instanceProvider = provider;
        initialized.set(true);
    }

    public static void loadFinished(BananaSpringInstanceProvider provider) {
        setInstanceProvider(provider);
        loadFinished.set(true);
    }

    public static boolean isLoadfinished() {
        return loadFinished.get();
    }

    /**
     * @param '[beanClass 对象的类]
     * @return T 类型为T的对象实例
     * @class_name BananaInstanceFactory
     * @method getInstance
     * @desc 获取指定类型的对象实例。如果IoC容器没配置好或者IoC容器中找不到该类型的实例则抛出异常。
     * @author free.zhang
     * @date 2018/5/17 15:23
     */
    public static <T> T getInstance(Class<T> beanClass) {
        return (T) getInstanceProvider().getInstance(beanClass);
    }

    /**
     * @param '[beanClass 实现类在容器中配置的名字, beanName 对象的类]
     * @return T  类型为T的对象实例
     * @class_name BananaInstanceFactory
     * @method getInstance
     * @desc 获取指定类型的对象实例。如果IoC容器没配置好或者IoC容器中找不到该实例则抛出异常。
     * @author free.zhang
     * @date 2018/5/17 15:22
     */
    public static <T> T getInstance(Class<T> beanClass, String beanName) {
        return (T) getInstanceProvider().getInstance(beanClass, beanName);
    }

    /**
     * @param '[beanName 实现类在容器中配置的名字]
     * @return T 对象的类型
     * @class_name BananaInstanceFactory
     * @method getInstance
     * @desc 获取指定类型的对象实例
     * @author free.zhang
     * @date 2018/5/17 15:22
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(String beanName) {

        return (T) getInstanceProvider().getInstance(beanName);
    }

    /**
     * @param '[]
     * @return com.burgess.banana.spring.BananaSpringInstanceProvider 实体提供者的一个实现类。
     * @class_name BananaInstanceFactory
     * @method getInstanceProvider
     * @desc 获取实例提供者。
     * @author free.zhang
     * @date 2018/5/17 15:21
     */
    public static BananaSpringInstanceProvider getInstanceProvider() {

        return instanceProvider;
    }

    public static ApplicationContext getContext() {
        return getInstanceProvider().getApplicationContext();
    }

    /**
     * @param '[]
     * @return void
     * @class_name BananaInstanceFactory
     * @method waitUtilInitialized
     * @desc 这是一个阻塞方法，直到context初始化完成
     * @author free.zhang
     * @date 2018/5/17 15:21
     */
    public synchronized static void waitUtilInitialized() {

        if (initialized.get()) return;
        while (true) {
            if (initialized.get()) break;
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            long waiting = System.currentTimeMillis() - timeStarting;
            if (waiting > 60 * 1000) throw new RuntimeException("Spring Initialize failture");
            System.out.println("Spring Initializing >>>>>" + waiting + " s");
        }
    }

    public static boolean isInitialized() {
        return initialized.get();
    }
}
