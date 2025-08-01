package com.ms.userServices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AdminDetails")
public class AdminDetails {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String userName;
	    private String password;
	    private String role;
	    
	    @Column(name = "failed_attempts")
	    private int failedAttempts = 0;

	    @Column(name = "account_locked")
	    private boolean accountLocked = false;
	    
	    @Column(unique = true)
	    private String email;
	    
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public int getFailedAttempts() {
			return failedAttempts;
		}
		public void setFailedAttempts(int failedAttempts) {
			this.failedAttempts = failedAttempts;
		}
		public boolean isAccountLocked() {
			return accountLocked;
		}
		public void setAccountLocked(boolean accountLocked) {
			this.accountLocked = accountLocked;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}

}
