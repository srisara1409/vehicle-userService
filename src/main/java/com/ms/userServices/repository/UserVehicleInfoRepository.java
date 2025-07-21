package com.ms.userServices.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.userServices.entity.UserVehicleInfo;

public interface UserVehicleInfoRepository extends JpaRepository<UserVehicleInfo, Long> {

	Optional<UserVehicleInfo> findByRegistrationNumber(String registrationNumber);
}
