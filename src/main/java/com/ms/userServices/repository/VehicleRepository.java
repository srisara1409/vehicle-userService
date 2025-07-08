package com.ms.userServices.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.userServices.entity.VehicleDetails;

public interface VehicleRepository extends JpaRepository<VehicleDetails, Long> {

	Optional<VehicleDetails> findByRegistrationNumber(String registrationNumber);
}
