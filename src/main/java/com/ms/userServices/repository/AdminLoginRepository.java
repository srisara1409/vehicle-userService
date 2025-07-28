package com.ms.userServices.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ms.userServices.entity.AdminDetails;

public interface AdminLoginRepository extends JpaRepository<AdminDetails, Long> {
	Optional<AdminDetails> findByUserNameIgnoreCase(String userName);
	
	Optional<AdminDetails> findByEmailIgnoreCase(String email);

}