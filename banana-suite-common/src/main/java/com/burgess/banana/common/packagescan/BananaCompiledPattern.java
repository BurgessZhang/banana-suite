package com.burgess.banana.common.packagescan;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.packagescan
 * @file BananaCompiledPattern.java
 * @time 2018-05-16 16:40
 * @desc
 */
public interface BananaCompiledPattern {

    String getOriginal();

    boolean matches(String value);
}
