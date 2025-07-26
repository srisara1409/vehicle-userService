package com.ms.userServices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ms.userServices.DTO.InactiveVehicleDTO;
import com.ms.userServices.entity.UserVehicleInfo;

public interface UserVehicleInfoRepository extends JpaRepository<UserVehicleInfo, Long> {

	Optional<UserVehicleInfo> findByRegistrationNumber(String registrationNumber);

	@Query("SELECT v.userVehicleId AS userVehicleId, v.registrationNumber AS registrationNumber, " +
		       "v.vehicleModel AS vehicleModel, v.vehicleMake AS vehicleMake, " +
		       "v.vehicleYear AS vehicleYear, v.fuelType AS fuelType, " +
		       "v.vehicleStatus AS vehicleStatus, v.note AS note " +
		       "FROM UserVehicleInfo v WHERE v.user.id = :userId AND v.vehicleStatus = 'InActive'")
		List<InactiveVehicleDTO> findInactiveVehiclesByUserId(@Param("userId") Long userId);
	
	List<UserVehicleInfo> findByUserIdAndVehicleStatusIgnoreCase(Long userId, String vehicleStatus);

}
