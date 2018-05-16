package com.burgess.banana.common.crypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author burgess.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.crypt
 * @file BananaAES.java
 * @time 2018/05/16 上午11:28
 * @desc AES加密
 */
public class BananaAES {


    /**
     * @class BananaAES.java
     * @method initKey
     * @author burgess.zhang
     * @time 18-5-16 上午11:34
     * @desc 生成秘钥
     * @param '[]
     * @return byte[]
     */
    public static byte[] initKey() throws Exception{
        //秘钥生成器
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        //初始化秘钥生成器:默认128，获得无政策权限后可用192或256
        keyGen.init(128);
        //生成秘钥
        SecretKey secretKey = keyGen.generateKey();

        return secretKey.getEncoded();
    }

    /**
     * @class BananaAES.java
     * @method encrypt
     * @author burgess.zhang
     * @time 18-5-16 上午11:38
     * @desc 加密
     * @param '[data, key]
     * @return byte[]
     */
    public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
        //恢复秘钥
        SecretKey secretKey = new SecretKeySpec(key,"AES");
        //cipher完成加密
        Cipher cipher = Cipher.getInstance("AES");
        //根据秘钥对cipher进行初始化
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        //加密
        byte[] encrypt = cipher.doFinal(data);

        return encrypt;
    }

    /**
     * @class BananaAES.java
     * @method decrypt
     * @author burgess.zhang
     * @time 18-5-16 上午11:42
     * @desc 解密
     * @param '[data, kye]
     * @return byte[]
     */
    public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
        //恢复秘钥生成器
        SecretKey secretKey = new SecretKeySpec(key,"AES");
        //Cipher完成解密
        Cipher cipher = Cipher.getInstance("AES");
        //根据秘钥对cipher进行初始化
        cipher.init(Cipher.DECRYPT_MODE,secretKey);
        //解密
        byte[] plain = cipher.doFinal(data);

        return plain;
    }

}
