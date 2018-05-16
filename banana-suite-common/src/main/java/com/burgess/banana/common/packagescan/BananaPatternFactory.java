package com.burgess.banana.common.packagescan;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.packagescan
 * @file BananaPatternFactory.java
 * @time 2018-05-16 16:40
 * @desc
 */
public interface BananaPatternFactory {

    BananaCompiledPattern compile(String pattern);
}
