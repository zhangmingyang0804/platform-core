package cn.platform.core.util;

import cn.platform.core.constants.CommonConstants;
import cn.platform.core.property.PropertyUtils;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * @Description: des对称加密工具类
 * @Package: cn.platform.core.util
 * @ClassName: Des3Utils
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:51
 * @Version: 1.0
 */
public class Des3Utils {
    private final static String SECRET_KEY = "$abcdefghij#,1234567890@";// 密钥 长度不得小于24
    private final static String IV = "1@#$qaz&";// 向量 可有可无 终端后台也要约定
    private final static String CIPHER_INSTANCE = "desede/CBC/PKCS5Padding";//

    private static final String DES_SECRET_KEY = "des.secret.key";


    /**
     * 加密
     *
     * @param plainText
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
        IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, getKey(), ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(CommonConstants.COMMON_ENCODING));
        return Base64.encodeBase64String(encryptData);
    }

    /**
     * 解密
     *
     * @param encryptText
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
        IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, getKey(), ips);
        byte[] decryptData = cipher.doFinal(Base64.decodeBase64(encryptText));
        return new String(decryptData, CommonConstants.COMMON_ENCODING);
    }

    /**
     * 获取key
     *
     * @return
     * @throws Exception
     */
    private static Key getKey() throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(PropertyUtils.getStringValue(DES_SECRET_KEY, SECRET_KEY).getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        return keyfactory.generateSecret(spec);
    }

    /**
     * test
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String encode = encode("23213123uyr");
        System.err.println(encode);
        String decode = decode(encode);
        System.err.println(decode);
    }
}