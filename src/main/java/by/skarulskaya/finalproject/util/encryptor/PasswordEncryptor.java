package by.skarulskaya.finalproject.util.encryptor;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordEncryptor {
    private PasswordEncryptor(){}
    public static String encrypt(String password) {
        return DigestUtils.md5Hex(password);
    }
}
