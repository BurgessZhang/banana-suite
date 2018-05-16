package com.burgess.banana.common.util;

import com.burgess.banana.common.crypt.BananaBase64;
import com.burgess.banana.common.crypt.BananaDES;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.util
 * @file BananaSimpleCryptUtils.java
 * @time 2018-05-16 16:56
 * @desc
 */
public class BananaSimpleCryptUtils {

    private static final String KEY_TAIL = "j@";

    public static String encrypt(String key,String data) throws Exception{
        key = BananaDigestUtils.md5Short(key) + KEY_TAIL;
        String encode = BananaDES.encrypt(key, data);
        byte[] bytes = BananaBase64.encodeToByte(encode.getBytes(StandardCharsets.UTF_8), true);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String decrypt(String key,String data) throws Exception{
        key = BananaDigestUtils.md5Short(key) + KEY_TAIL;
        byte[] bytes = BananaBase64.decode(data);
        data = new String(bytes, StandardCharsets.UTF_8);
        return BananaDES.decrypt(key, data);
    }


    public static void main(String[] args) throws Exception {
        long s = System.currentTimeMillis();
        for (int i = 0; i < 2; i++) {
            String key = UUID.randomUUID().toString().replaceAll("-", "");
            String data = UUID.randomUUID().toString().replaceAll("-", "").substring(0,16);
            String encode = encrypt(key, data);
            if(!data.equals(decrypt(key, encode))){
                System.out.println(encode);
            }

        }

        System.out.println(System.currentTimeMillis() - s );
    }
}
