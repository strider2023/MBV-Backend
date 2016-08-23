package com.mbv.framework.util;

import com.mbv.framework.props.EmailProps;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;

public class SMTP {

    @Autowired
    private EmailProps emailProps;


    public SMTP() {

    }

    public EmailProps getEmailProps() {
        return emailProps;
    }

    public void setEmailProps(EmailProps emailProps) {
        this.emailProps = emailProps;
    }

    public void sendMail(String toEmail, String subject, String body) {
        sendMail(toEmail, subject, body, null, null, null);
    }

    public void sendMail(String toEmail, String subject, String body, String fromMail) {
        sendMail(toEmail, subject, body, null, null, fromMail);
    }


    public void sendMail(String toEmail, String subject, String body, byte[] attachment, String attachmentName, String fromMail) {
        try {

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", emailProps.getHost());
            props.put("mail.smtp.port", emailProps.getPort());

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailProps.getUser(), emailProps.getPassword());
                }
            });


            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromMail != null ? fromMail : emailProps.getReplyFrom()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            //Body Message
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html; charset=utf-8");
            multipart.addBodyPart(messageBodyPart);

            //Attachment
            if (attachment != null) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(attachment, "application/pdf");
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(attachmentName);
                multipart.addBodyPart(messageBodyPart);
            }

            message.setContent(multipart, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
