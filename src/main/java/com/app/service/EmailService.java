package com.app.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	public boolean sendEmail(String subject,String message, String to) {
		//rest of the code
		boolean f = false;
		
        String from = "pahadirealo1@gmail.com";
        
        //variable for gmail
      		String host = "smtp.gmail.com";
      		
      		//getting the system properties
      		Properties properties = System.getProperties();
      		System.out.println("properties :" +properties);
      		
      		//setting important information to properties object
      		//setting host
      		properties.put("mail.smtp.host", host);
      		properties.put("mail.smtp.port", "465");
      		properties.put("mail.smtp.ssl.enable", "true");
      		properties.put("mail.smtp.auth", "true");
      		
      		//step 1: get the session object
      		Session session = Session.getInstance(properties, new Authenticator() {
      		
      				protected PasswordAuthentication getPasswordAuthentication() {
      					return new PasswordAuthentication("pahadirealo1@gmail.com", "vivekdhyani3");
      					
      				}
      		});
      		session.setDebug(true);
      		//step2 : compose the message
      		MimeMessage mimeMessage = new MimeMessage(session);
      		
      		try {
      			//from
      			mimeMessage.setFrom(from);
      			//adding recipient
      			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      			//adding subject to message
      			mimeMessage.setSubject(subject);
      			//adding text to message
      			mimeMessage.setContent(message,"text/html");
      			
      			//step 3: sending the message using Transport class
      			Transport.send(mimeMessage);
      			System.out.println("message send successfully");
      			f = true;
      		}
      		catch (Exception e) {
      			e.printStackTrace();
      		}
      		return f;
	}
}
