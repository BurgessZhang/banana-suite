package com.burgess.banana.spring;

import org.springframework.context.ApplicationContext;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.spring
 * @file BananaApplicationStartedListener.java
 * @time 2018-05-17 15:24
 * @desc 应用启动完成监听器接口
 */
public interface BananaApplicationStartedListener {

    void onApplicationStarted(ApplicationContext applicationContext);

}
