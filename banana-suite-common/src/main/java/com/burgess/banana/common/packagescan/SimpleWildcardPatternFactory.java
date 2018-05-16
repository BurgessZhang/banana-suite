package com.burgess.banana.common.packagescan;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.packagescan
 * @file SimpleWildcardPatternFactory.java
 * @time 2018-05-16 16:39
 * @desc
 */
public class SimpleWildcardPatternFactory implements BananaPatternFactory {

    public BananaCompiledPattern compile(String pattern) {
        return new BananaSimpleWildcardPattern(pattern);
    }
}
