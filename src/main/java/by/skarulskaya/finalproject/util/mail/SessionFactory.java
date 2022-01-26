package by.skarulskaya.finalproject.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

class SessionFactory {
    public static Session createSession(Properties properties) {
        String userName = properties.getProperty("mail.user.name"); //todo
        String userPassword = properties.getProperty("mail.user.password");
        return Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, userPassword);
            }
        });
    }
}
