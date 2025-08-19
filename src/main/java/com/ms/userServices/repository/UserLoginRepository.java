package com.ms.userServices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ms.userServices.DTO.UserInfoDTO;
import com.ms.userServices.entity.UserInfo;

import jakarta.transaction.Transactional;

public interface UserLoginRepository extends JpaRepository<UserInfo, Long> {

	@Query("""
			    SELECT new com.ms.userServices.DTO.UserInfoDTO(
			        u.id, u.firstName, u.lastName, u.dateOfBirth, u.mobileNumber, u.email, u.vehicleType , u.licenseNumber, u.status, u.createdAt, 
			        u.updatedAt
			    )
			    FROM UserInfo u
			""")
	List<UserInfoDTO> findAllUserSummaries();

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	@Query(value = """
	  UPDATE user_info u
	  SET status = 'CLOSED',
	  updated_at = now()
	  WHERE u.status = 'APPROVED'
	    AND EXISTS (SELECT 1 FROM user_vehicle_info v WHERE v.user_id = u.id)
	    AND NOT EXISTS (SELECT 1 FROM user_vehicle_info v
	                    WHERE v.user_id = u.id AND LOWER(BTRIM(v.vehicle_status)) = 'active')
	    AND NOT EXISTS (SELECT 1 FROM user_vehicle_info v
	                    WHERE v.user_id = u.id AND (v.bond_end_date IS NULL OR BTRIM(v.bond_end_date) = ''))
	    AND NOT EXISTS (SELECT 1 FROM user_vehicle_info v
	                    WHERE v.user_id = u.id AND parse_end_ts(v.bond_end_date) > now())
	  """, nativeQuery = true)
	int closeEligibleApprovedUsers();


}