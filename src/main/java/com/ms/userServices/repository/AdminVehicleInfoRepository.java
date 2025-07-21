package com.ms.userServices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.userServices.entity.AdminVehicleInfo;

public interface AdminVehicleInfoRepository extends JpaRepository<AdminVehicleInfo, Long> {
	
	List<AdminVehicleInfo> findByRegistrationNumberContainingIgnoreCaseAndVehicleStatus(String regNumberPart, String status);
	
	boolean existsByRegistrationNumber(String registrationNumber);
	
	Optional<AdminVehicleInfo> findByRegistrationNumber(String registrationNumber);
}

