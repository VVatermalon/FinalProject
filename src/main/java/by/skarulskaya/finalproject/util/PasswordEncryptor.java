package by.skarulskaya.finalproject.util;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordEncryptor {
    private PasswordEncryptor(){}
    public static String encrypt(String password) {
        return DigestUtils.sha512Hex(password);
    }
}
