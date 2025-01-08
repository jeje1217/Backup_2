package com.example.camping.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.camping.entity.Users;
import com.example.camping.userService.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {
	
	private UserService userService;
	
	// 로그인 폼
	@GetMapping("/login")
	public String loginForm() {
		return "users/login/login";
	}

	// 비밀번호 변경 폼
	@GetMapping("/change-password")
	public String changePasswordForm() {
		return "users/login/change-password";
	}
	
	// 비밀번호 변경 처리
	@PostMapping("/change-password")
	public String changePassword(@RequestParam String oldPassword,
								 @RequestParam String newPassword,
								 @AuthenticationPrincipal User principal) {
		
		String userId = principal.getUsername();
		
		Users user = userService.findByUserId(userId);
		
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		
		// 기존 비밀번호가 일치하면 비밀번호 변경
		if(user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
			// 비밀번호 암호화 후 저장
			user.setPassword(passwordEncoder.encode(newPassword));
			userService.save(user);
		}else {
			return "users/login/change-password";
		}
		
		return "redirect:/users/profile/profile";
		
	}
	
	/*
	// 비밀번호 재설정 링크 요청 폼
    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "users/login/forgot-password"; // 비밀번호 재설정 링크 요청 폼 반환
    }
    
    // 비밀번호 재설정 요청 처리
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String userId, Model model) {
        // 비밀번호 재설정 메일 보내기 처리 (SMTP나 외부 서비스 연동)
        Users user = userService.findByUserId(userId);
        if (user != null) {
            // 실제 비밀번호 재설정 메일 발송 로직을 작성 (SMTP 등)
            model.addAttribute("message", "비밀번호 재설정 링크를 이메일로 보냈습니다.");
        } else {
            model.addAttribute("error", "사용자를 찾을수 없습니다.");
        }
        return "users/login/forgot-password"; // 다시 폼을 반환
    }
   	*/
	
	// 현재 로그인된 사용자 정보 로그
	@GetMapping("/loginSuccess")
	public String loginSuccess(@RequestParam String userId, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null) {
	    	String currentUser = authentication.getName();
	    	
	    	String authorities = authentication.getAuthorities().stream()
	    									   .map(authority -> authority.getAuthority())
	    									   .reduce((a, b) -> a + ", " + b)
	    									   .orElse("권한없음");
	    	
	    	log.info("현재 로그인된 사용자: {}, 권한: {}", currentUser, authorities);
	    	
	    	model.addAttribute("currentUser", currentUser);
	    	model.addAttribute("authorities", authorities);
	    
	    } else {
	    	log.warn("로그인된 사용자가 없습니다.");
	    }
	     
	     return "users/profile/profile";
	        
	}
	
	@GetMapping("/loginFail")
	public String loginFail(Model model) {
		model.addAttribute("error", "로그인 실패");
		return "users/login/login";
	}
	
	
	
	
}
