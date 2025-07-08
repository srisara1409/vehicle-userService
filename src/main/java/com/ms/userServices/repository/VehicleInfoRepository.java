package com.ms.userServices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.userServices.entity.VehicleInfo;

public interface VehicleInfoRepository extends JpaRepository<VehicleInfo, Long> {
	
	List<VehicleInfo> findByRegistrationNumberContainingIgnoreCaseAndStatus(String regNumberPart, String status);
	
	boolean existsByRegistrationNumber(String registrationNumber);
}


