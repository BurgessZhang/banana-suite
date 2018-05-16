package com.burgess.banana.common.sequence;

/**
 * @author burgess.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.sequence
 * @file BananaIdGenerator.java
 * @time 2018-05-16 20:52
 * @desc 全局ID生成器ID
 */
public interface BananaIdGenerator {

    long nextId();

    void close();
}
