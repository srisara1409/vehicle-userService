package com.ms.userServices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.userServices.entity.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Long> {

}