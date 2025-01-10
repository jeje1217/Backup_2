package com.example.camping.config;

import org.springframework.stereotype.Component;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PasswordEmailService {
	
	private final JavaMailSender mailSender;
	
	// 이메일 발송 메서드
	public Boolean sendResetCodeEmail(String email, String resetCode) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			
			helper.setTo(email);
			helper.setSubject("비밀번호 재설정 링크");
			helper.setText("비밀번호 재설을 위한 코드는: " + resetCode + 
						   "\n" + "해당 코드를 비밀번호 재설정 페이지에 입력해주세요");
			
			mailSender.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
			
		}
	}
}
