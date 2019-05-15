package org.sienkiewicz.conferenceapp.mailsender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
	
	@Autowired
	private JavaMailSender mailSender;
	
	private final String _TITLE = "Sii Internship 2019";
	private final String _MESSAGE = "YOU'VE SUCCESFULY ASSIGNED TO LECTURE! SEE U!";
	
	/**
	 *  Send email to assigned user. 
	 * @param to	user email
	 */
    public void sendEmail(String to) {
        MimeMessage mail = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setReplyTo(to);
            helper.setFrom("siiinternshipconference@sienkiewicz-maciej.pl");
            helper.setSubject(_TITLE);
            helper.setText(_MESSAGE, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        
        mailSender.send(mail);
    }
}

