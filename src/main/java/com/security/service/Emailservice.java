package com.security.service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class Emailservice {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String senderEmail;

	public void sendActivationLink(String email, String firstName, String activationLink) {
		String ACTIVATION_EMAIL_TEMPLATE = "templates/activate-account.html";


		String subject = "Activate Your Account";

		sentEmailWithTemplate(email, firstName, subject, activationLink);
	}

	public void sendResetPasswordRequestToUser(String email, String firstName, String resetPasswordLink) {
		String RESET_PASSWORD_EMAIL_TEMPLATE = "templates/reset-password.html";

		String subject = "Reset Your Password";

		sentEmailWithTemplate(email, firstName, subject, resetPasswordLink);
	}

	public void sentEmailWithTemplate(String email, String firstName, String subject, String url) {

		String senderName = "Spring Boot 3 Team";
		String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(senderEmail, senderName);
			helper.setTo(email);
			helper.setSubject(subject);

			String content=url;
                   // ="Dear [[firstName]],<br>"+"please click link for verification"+"<h3><a href=\"[[activationLink]]\" target=\"_self\">VERYFY</a></h3>"
                    //+"Thank you"
                   // ;
			// Load email template from file
			/*ClassPathResource resource = new ClassPathResource(template);
			String content = new String(Files.readAllBytes(resource.getFile().toPath()));*/

			// Replace placeholders in email template with dynamic content
			content = content.replace("{{firstName}}", firstName);
			content = content.replace("{{activationLink}}", url);
			content = content.replace("{{currentYear}}", currentYear);



			helper.setText(content, true);

			javaMailSender.send(message);

            System.out.println(url);

		} catch (MessagingException | IOException exception) {
			exception.printStackTrace();
		}
	}

}
