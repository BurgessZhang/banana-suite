package com.burgess.banana.common.crypt;


import com.burgess.banana.common.exception.BananaSuiteException;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.crypt
 * @file BananaSHA1.java
 * @time 2018-05-16 15:29
 * @desc SHA1加密
 */
public class BananaSHA1 {

    /**
     * @param '[token 票据, timestamp 时间戳, nonce 随机字符串, encrypt 密文]
     * @return java.lang.String 安全签名
     * @class_name BananaSHA1
     * @method getSHA1
     * @desc 用SHA1算法生成安全签名
     * @author free.zhang
     * @date 2018/5/16 15:31
     */
    public static String getSHA1(String token, String timestamp, String nonce, String encrypt) {
        try {
            String[] array = new String[]{token, timestamp, nonce, encrypt};
            StringBuffer sb = new StringBuffer();

            /* 字符串排序 */
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }

            /* SHA1签名生成 */
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(sb.toString().getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }

            return hexstr.toString();
        } catch (Exception e) {
            throw new BananaSuiteException(500, "error", e);
        }
    }
}
