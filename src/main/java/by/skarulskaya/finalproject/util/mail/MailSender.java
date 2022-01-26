package by.skarulskaya.finalproject.util.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum MailSender {
    INSTANCE;
    private final Logger logger = LogManager.getLogger();
    private static final String MAIL_PROPERTIES_PATH = "dataSrc/mail.properties";
    private final Properties properties;

    MailSender() {
        properties = new Properties();
        ClassLoader loader = this.getClass().getClassLoader();
        InputStream resourceStream = loader.getResourceAsStream(MAIL_PROPERTIES_PATH);
        try {
            properties.load(resourceStream);
        } catch (IOException e) {
            logger.fatal("Error during mail sender setting", e);
            throw new RuntimeException("Error during mail sender setting", e);
        }
    }

    public void send(String sendToMail, String mailSubject, String mailText) throws Exception {
        try {
            MimeMessage message = initMessage(sendToMail, mailSubject, mailText);
            Transport.send(message);
        } catch (Exception e) {
            logger.error(e);
            throw new Exception(e); //todo throw own
        }
    }

    private MimeMessage initMessage(String sendToMail, String mailSubject, String mailText) throws Exception {
        MimeMessage message;
        Session mailSession = SessionFactory.createSession(properties);
        mailSession.setDebug(true);
        message = new MimeMessage(mailSession);
        try {
            message.setSubject(mailSubject);
            message.setContent(mailText, "text/html");
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendToMail));
        } catch (MessagingException e) {
            logger.error(e);
            throw new Exception(e); //todo create own exception
        }
        return message;
    }
}
