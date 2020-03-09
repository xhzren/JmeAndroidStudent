package cn.xhzren.nettytest.connection.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.password.BasicPasswordEncryptor;

public class JasyptUtil {

    public static String encryptor(String plainText){
        // 创建加密器
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        return encryptor.encryptPassword(plainText);
    }

    public static boolean checkCry(String plainText, String ciphertext){
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        return encryptor.checkPassword(plainText,ciphertext);
    }

}
