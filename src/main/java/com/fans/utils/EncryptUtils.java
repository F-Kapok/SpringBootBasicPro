package com.fans.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * @ClassName EncryptUtils
 * @Description: TODO 文本安全加密工具（MD5,AES）
 * @Author fan
 * @Date 2018-09-10 13:44
 * @Version 1.0
 **/
public class EncryptUtils {

    /**
     * @Description: TODO spring md5加密工具
     * @Param: [content]
     * @return: java.lang.String
     * @Author: fan
     * @Date: 2018/09/10 15:35
     **/
    public static String MD5Encrypt(String content) {
        String md5EncryptStr = DigestUtils.md5DigestAsHex(content.getBytes());
        return md5EncryptStr;
    }

    /**
     * @Description: TODO spring md5加密工具 增加编码格式
     * @Param: [content]
     * @return: java.lang.String
     * @Author: fan
     * @Date: 2018/09/10 15:35
     **/
    public static String MD5Encrypt(String content, String charset) {
        String md5EncryptStr = null;
        try {
            md5EncryptStr = DigestUtils.md5DigestAsHex(content.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return md5EncryptStr;
    }
    
    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     */
    private static byte[] AESEncode(String content, String encryptKey) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, new SecureRandom(encryptKey.getBytes()));
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyGenerator.generateKey().getEncoded(), "AES"));
            byte[] AES_decode = cipher.doFinal(content.getBytes("utf-8"));
            return AES_decode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * base 64 加密
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    private static String base64Encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     */
    public static String getAesEncrypt(String content, String encryptKey) {
        return base64Encode(AESEncode(content, encryptKey));
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    private static String AESDecode(byte[] encryptBytes, String decryptKey) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(decryptKey.getBytes()));

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
            byte[] decryptBytes = cipher.doFinal(encryptBytes);
            return new String(decryptBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * base 64 解密
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    private static byte[] base64Decode(String base64Code) throws Exception {
        return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String getAesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return StringUtils.isEmpty(encryptStr) ? null : AESDecode(base64Decode(encryptStr), decryptKey);
    }

}
