package com.example.insurancemobileapp;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail {

    public static void sendmail(String emailTo, String policy) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("osiguruvanje@outlook.com", "WebServisi2021");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("osiguruvanje@outlook.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
        msg.setSubject("Потврда за полиса со код: " + policy.split("\\|")[0]);
        msg.setContent("PISI MI ODMAAA", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Ви благодариме што не избравте нас :) \n", "text/plain;charset=UTF-8");

        MimeBodyPart policyText = new MimeBodyPart();
        policyText.setContent(policy, "text/plain;charset=UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(policyText);
        // MimeBodyPart attachPart = new MimeBodyPart();
        // attachPart.attachFile("/var/tmp/image19.png");
        // multipart.addBodyPart(attachPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }

    public static void sendMailConfirm(String emailTo) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("osiguruvanje@outlook.com", "WebServisi2021");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("osiguruvanje@outlook.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
        msg.setSubject("Успешно ");
        msg.setContent("Почитувани, \n\nУспешно се регистриравте на StaySafe Insurance!", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Ви благодариме што нé избравте нас! \n", "text/plain;charset=UTF-8");

//        MimeBodyPart policyText = new MimeBodyPart();
//        policyText.setContent(policy, "text/plain;charset=UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
//        multipart.addBodyPart(policyText);
        // MimeBodyPart attachPart = new MimeBodyPart();
        // attachPart.attachFile("/var/tmp/image19.png");
        // multipart.addBodyPart(attachPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }
}
