package com.burgess.banana.common.crypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author burgess.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.crypt
 * @file BananaDES.java
 * @time 2018/05/16 下午2:15
 * @desc 加密解密工具类
 */
public class BananaDES {

    private final static byte[] IV_PARAMS_BYTES = "bananasuite".getBytes();
    private final static String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    /**
     * @class BananaDES.java
     * @method encrypt
     * @author burgess.zhang
     * @time 18-5-16 下午2:17
     * @desc DES算法加密
     * @param '[key 加密私钥，长度不能小于8位, data 待加密字符串]
     * @return java.lang.String 加密后的字节数组，一般结婚Base64编码使用
     */
    public static String encrypt(String key,String data) throws Exception{
        if (null == data){
            return null;
        }
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        AlgorithmParameterSpec parameterSpec = new IvParameterSpec(IV_PARAMS_BYTES);
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,parameterSpec);
        byte[] bytes = cipher.doFinal(data.getBytes());
        return byte2hex(bytes);
    }

    public static String decrypt(String key,String data)throws Exception{
        /**
         * @class BananaDES.java
         * @method decrypt
         * @author burgess.zhang
         * @time 18-5-16 下午2:27
         * @desc DES算法解密
         * @param '[key 解密秘钥，长度不能够小于8位, data 待解密字符串]
         * @return java.lang.String 解密后的字节数组
         */
        if (null == data){
            return null;
        }
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        AlgorithmParameterSpec parameterSpec = new IvParameterSpec(IV_PARAMS_BYTES);
        cipher.init(Cipher.DECRYPT_MODE,secretKey,parameterSpec);
        return new String(cipher.doFinal(hex2byte(data.getBytes())));
    }


    /**
     * @class BananaDES.java
     * @method byte2hex
     * @author burgess.zhang
     * @time 18-5-16 下午2:31
     * @desc 二进制转字符串
     * @param '[bytes]
     * @return java.lang.String
     */
    private static String byte2hex(byte[] bytes){

        StringBuilder builder = new StringBuilder();
        String stmp = null;
        for(int n = 0; null != bytes && n <bytes.length;n++){
            stmp = Integer.toHexString(bytes[n] & 0XFF);
            if (stmp.length() == 1){
                builder.append('0');
            }
            builder.append(stmp);
        }
        return builder.toString().toUpperCase();
    }

    private static String hex2byte(byte[] bytes){
        if ((bytes.length % 2) != 0){
            throw new IllegalArgumentException();
        }
        byte[] bytes1 = new byte[bytes.length /2];
        for(int n =0; n<bytes.length;n+=2){
            String item = new String(bytes,n,2);
            bytes1[n/2] = (byte)Integer.parseInt(item,16);
        }
        return bytes1;
    }

}
