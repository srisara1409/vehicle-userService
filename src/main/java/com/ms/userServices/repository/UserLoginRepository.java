package com.ms.userServices.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.userServices.entity.UserInfo;

public interface UserLoginRepository extends JpaRepository<UserInfo, Long> {
	//Optional<UserInfo> findByUserName(String userName);

}