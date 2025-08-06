package com.ms.userServices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ms.userServices.DTO.UserInfoDTO;
import com.ms.userServices.entity.UserInfo;

public interface UserLoginRepository extends JpaRepository<UserInfo, Long> {
	
	@Query("""
		    SELECT new com.ms.userServices.DTO.UserInfoDTO(
		        u.id, u.firstName, u.lastName, u.email, u.mobileNumber, u.status
		    )
		    FROM UserInfo u
		""")
		List<UserInfoDTO> findAllUserSummaries();

}