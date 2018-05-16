package com.burgess.banana.common.packagescan;

import java.util.regex.Pattern;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.packagescan
 * @file BananaSimpleWildcardPattern.java
 * @time 2018-05-16 16:42
 * @desc
 */
public class BananaSimpleWildcardPattern implements BananaCompiledPattern {

    private Pattern pattern;
    private String original;

    public BananaSimpleWildcardPattern(String pattern) {

        this.original = pattern;

        String ptn = pattern;
        ptn = ptn.replace(".", "\\.");
        ptn = ptn.replace("*", ".*");
        this.pattern = Pattern.compile(ptn);
    }


    @Override
    public String getOriginal() {
        return original;
    }

    @Override
    public boolean matches(String value) {
        return pattern.matcher(value).matches();
    }
}
