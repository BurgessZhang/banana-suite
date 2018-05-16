package com.burgess.banana.common.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.burgess.banana.common.crypt.BananaDES;
import com.burgess.banana.common.exception.BananaSuiteException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.util
 * @file BananaTokenGenerator.java
 * @time 2018-05-16 16:54
 * @desc token生成器
 */
public class BananaTokenGenerator {

    private static final String LINE_THROUGH = "-";
    private static final int EXPIRE = 1000*60*3;
    private static final String DES_CRYPT_KEY = BananaResourceUtils.getProperty("des.crypt.key");

    public static String generate(String...prefixs){
        String str = StringUtils.replace(UUID.randomUUID().toString(), LINE_THROUGH, StringUtils.EMPTY);
        if(prefixs != null && prefixs.length > 0 &&  StringUtils.isNotBlank(prefixs[0])){
            return prefixs[0].concat(str);
        }
        return str;
    }

    /**
     * 生成带签名信息的token
     * @return
     */
    public static String generateWithSign() throws Exception{
        Date date = new Date();
        String cryptKey = getCryptKey(date);
        String str = BananaDigestUtils.md5Short(generate()).concat(String.valueOf(date.getTime()));
        return BananaDES.encrypt(cryptKey, str).toLowerCase();
    }


    /**
     * 验证带签名信息的token
     */
    public static void validate(String token,boolean validateExpire){
        long timestamp = 0;
        Date date = new Date();
        String cryptKey = getCryptKey(date);
        try {
            timestamp = Long.parseLong(BananaDES.decrypt(cryptKey,token).substring(6));
        } catch (Exception e) {
            throw new BananaSuiteException(4005, "格式不正确");
        }
        if(validateExpire && date.getTime() - timestamp > EXPIRE){
            throw new BananaSuiteException(4005, "token已过期");
        }
    }

    private static String getCryptKey(Date date){
        if(DES_CRYPT_KEY != null && DES_CRYPT_KEY.length() == 8){return DES_CRYPT_KEY;}
        SimpleDateFormat format = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
        String key = format.format(date).toUpperCase();
        key  = BananaDigestUtils.md5(key).substring(0,8);
        return key;
    }

    public static void main(String[] args) throws Exception{
        String generateWithSign = BananaTokenGenerator.generateWithSign();
        System.out.println(generateWithSign);

        BananaTokenGenerator.validate(generateWithSign, true);

    }
}
