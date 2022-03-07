package com.pizza.mail;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.pizza.model.vo.PizzaMenu;

public class SendMail {
	public static void main(String[] args) {
		SendMail test = new SendMail();
        test.gmailSend();
    }
    
    
    public void gmailSend() {
        String user = "id@gmail.com"; // 네이버일 경우 네이버 계정, gmail경우 gmail 계정
        String password = "password";   // 패스워드

        String to = "id@gmail.com";

        // SMTP 서버 정보를 설정한다.
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 465);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true"); 
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try (ObjectInputStream ois = 
				new ObjectInputStream(new BufferedInputStream(new FileInputStream("pizza.txt")));) {
        	
        	MimeMessage message = new MimeMessage(session);
        	PizzaMenu arr[] = (PizzaMenu[]) ois.readObject();
        	
        	for(PizzaMenu pizzas : arr) {
        		System.out.println(pizzas);
        		message.setText(pizzas.toString());
        	}
        	
            message.setFrom(new InternetAddress(user));

            //수신자메일주소
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); 

            // Subject
            message.setSubject("제목을 입력하세요"); //메일 제목을 입력

            // send the message
            Transport.send(message); ////전송
            System.out.println("message sent successfully...");
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
			e.printStackTrace();
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}