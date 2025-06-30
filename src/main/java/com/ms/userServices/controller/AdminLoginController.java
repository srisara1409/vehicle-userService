package com.ms.userServices.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        Optional<AdminDetails> userOpt = userRepo.findByUserName(request.getUserName());

        if (userOpt.isPresent()) {
        	AdminDetails user = userOpt.get();
            // In production, use hashed password (BCrypt)
            if (user.getPassword().equals(request.getPassword())) {
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    return ResponseEntity.ok("LOGIN_SUCCESS");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NOT_ADMIN");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("INVALID_PASSWORD");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("USER_NOT_FOUND");
        }
    }
}
