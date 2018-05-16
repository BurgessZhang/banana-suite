package com.burgess.banana.common.serizlizer;

import java.io.IOException;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.serizlizer
 * @file BananaSerializer.java
 * @time 2018-05-16 16:33
 * @desc 序列化接口
 */
public interface BananaSerializer {

    String name();

    byte[] serialize(Object obj) throws IOException;

    Object deserialize(byte[] bytes) throws IOException;
}
