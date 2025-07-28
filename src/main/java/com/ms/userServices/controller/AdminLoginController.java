package com.ms.userServices.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.userServices.entity.AdminDetails;
import com.ms.userServices.model.AdminLoginRequest;
import com.ms.userServices.repository.AdminLoginRepository;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminLoginController {

	@Autowired
    private AdminLoginRepository userRepo;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody AdminLoginRequest request) {
	    Optional<AdminDetails> userOpt = userRepo.findByUserNameIgnoreCase(request.getUserName());

	    if (userOpt.isPresent()) {
	        AdminDetails user = userOpt.get();

	        // Check if account is locked
	        if (user.isAccountLocked()) {
	            return ResponseEntity.status(HttpStatus.LOCKED).body("ACCOUNT_LOCKED");
	        }

	        if (user.getPassword().equals(request.getPassword())) {
	            // Reset failed attempts
	            user.setFailedAttempts(0);
	            userRepo.save(user);

	            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
	                return ResponseEntity.ok("LOGIN_SUCCESS");
	            } else {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NOT_ADMIN");
	            }
	        } else {
	            // Increment failed attempts
	            int attempts = user.getFailedAttempts() + 1;
	            user.setFailedAttempts(attempts);

	            if (attempts >= 3) {
	                user.setAccountLocked(true);
	            }

	            userRepo.save(user);
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("INVALID_PASSWORD");
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("USER_NOT_FOUND");
	    }
	}
	
	@PatchMapping("/admin/unlockAccount/{username}")
	public ResponseEntity<String> unlockAccount(@PathVariable String username) {
	    Optional<AdminDetails> userOpt = userRepo.findByUserNameIgnoreCase(username);

	    if (userOpt.isPresent()) {
	        AdminDetails user = userOpt.get();

	        user.setFailedAttempts(0);
	        user.setAccountLocked(false);
	        userRepo.save(user);

	        return ResponseEntity.ok("ACCOUNT_UNLOCKED");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER_NOT_FOUND");
	    }
	}
	
	@PostMapping("/verifyEmail")
	public ResponseEntity<String> verifyEmail(@RequestBody Map<String, String> request) {
	    String email = request.get("email");
	    Optional<AdminDetails> admin = userRepo.findByEmailIgnoreCase(email);

	    if (admin.isPresent()) {
	        return ResponseEntity.ok("VALID");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("INVALID");
	    }
	}
	
	@PostMapping("/resetPassword")
	public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
	    String email = request.get("email");
	    String newPassword = request.get("password");

	    Optional<AdminDetails> adminOpt = userRepo.findByEmailIgnoreCase(email);
	    if (adminOpt.isPresent()) {
	        AdminDetails admin = adminOpt.get();
	        admin.setPassword(newPassword);  // In production: hash password with BCrypt!
	        userRepo.save(admin);
	        return ResponseEntity.ok("Password updated successfully.");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
	    }
	}
}
