package com.ms.userServices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ms.userServices.DTO.InactiveVehicleDTO;
import com.ms.userServices.DTO.UserVehicleDTO;
import com.ms.userServices.entity.UserVehicleInfo;

public interface UserVehicleInfoRepository extends JpaRepository<UserVehicleInfo, Long> {

	//Optional<UserVehicleInfo> findByRegistrationNumber(String registrationNumber);
	
	  // Case-insensitive rego; only care about ACTIVE for other users
	  @Query("""
	      select count(v) > 0
	      from UserVehicleInfo v
	      where upper(v.registrationNumber) = upper(:reg)
	        and lower(v.vehicleStatus) = 'Active'
	        and v.user.id <> :userId
	      """)
	  boolean existsActiveByRegForOtherUser(@Param("reg") String reg, @Param("userId") Long userId);


	@Query("SELECT v.userVehicleId AS userVehicleId, v.registrationNumber AS registrationNumber, " +
		       "v.vehicleModel AS vehicleModel, v.vehicleMake AS vehicleMake, " +
		       "v.vehicleYear AS vehicleYear, v.fuelType AS fuelType, " +
		       "v.bondStartDate AS bondStartDate, v.bondEndDate AS bondEndDate, " +
		       "v.vehicleStatus AS vehicleStatus, v.updatedAt AS updatedAt, v.note AS note " +
		       "FROM UserVehicleInfo v WHERE v.user.id = :userId AND v.vehicleStatus = 'InActive'")
	List<InactiveVehicleDTO> findInactiveVehiclesByUserId(@Param("userId") Long userId);
	
	List<UserVehicleInfo> findByUserIdAndVehicleStatusIgnoreCase(Long userId, String vehicleStatus);

	@Query("""
		    SELECT new com.ms.userServices.DTO.UserVehicleDTO(
		        v.user.id, v.userVehicleId, v.registrationNumber, v.vehicleMake, v.vehicleModel, v.vehicleYear, v.bondStartDate, v.bondEndDate, v.updatedAt
		    )
		    FROM UserVehicleInfo v
		    WHERE v.user.id IN :userIds
		""")
	List<UserVehicleDTO> findVehiclesByUserIds(@Param("userIds") List<Long> userIds);
}
