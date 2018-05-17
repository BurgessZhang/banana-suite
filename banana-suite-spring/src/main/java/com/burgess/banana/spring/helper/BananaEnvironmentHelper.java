package com.burgess.banana.spring.helper;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.burgess.banana.spring.BananaInstanceFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.spring.helper
 * @file BananaEnvironmentHelper.java
 * @time 2018-05-17 15:29
 * @desc
 */
public class BananaEnvironmentHelper {

    private static Environment environment;

    public static String getProperty(String key) {
        init();
        return environment == null ? null : environment.getProperty(key);
    }

    public static void init() {
        if (environment == null && BananaInstanceFactory.isInitialized()) {
            synchronized (BananaEnvironmentHelper.class) {
                environment = BananaInstanceFactory.getInstance(Environment.class);
            }
        }
    }

    public static boolean containsProperty(String key) {
        init();
        return environment == null ? false : environment.containsProperty(key);
    }


    public static Map<String, Object> getAllProperties(String prefix) {
        init();
        if (environment == null) return null;
        MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();

        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        for (PropertySource<?> source : propertySources) {
            if (source.getName().startsWith("servlet") || source.getName().startsWith("system")) {
                continue;
            }
            if (source instanceof EnumerablePropertySource) {
                for (String name : ((EnumerablePropertySource<?>) source).getPropertyNames()) {
                    boolean match = StringUtils.isEmpty(prefix);
                    if (!match) {
                        match = name.startsWith(prefix);
                    }
                    if (match) {
                        Object value = source.getProperty(name);
                        if (value != null) {
                            properties.put(name, value);
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableMap(properties);
    }
}
