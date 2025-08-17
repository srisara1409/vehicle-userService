package com.ms.userServices.DTO;

import java.time.LocalDateTime;

public class UserVehicleDTO {
    private Long userId;
    private Long userVehicleId;
    private String registrationNumber;
    private String vehicleMake;
    private String vehicleModel;
    private Integer vehicleYear;
    private String bondStartDate;
    private String bondEndDate;
    private LocalDateTime updatedAt;

    public UserVehicleDTO(Long userId, Long userVehicleId, String registrationNumber, String vehicleMake, String vehicleModel, Integer vehicleYear, String bondStartDate, String bondEndDate, LocalDateTime updatedAt) {
        this.userId = userId;
        this.userVehicleId = userVehicleId;
        this.registrationNumber = registrationNumber;
        this.vehicleMake = vehicleMake;
        this.vehicleModel = vehicleModel;
        this.vehicleYear = vehicleYear;
        this.bondStartDate = bondStartDate;
        this.bondEndDate = bondEndDate;
        this.updatedAt = updatedAt;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserVehicleId() {
		return userVehicleId;
	}

	public void setUserVehicleId(Long userVehicleId) {
		this.userVehicleId = userVehicleId;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getVehicleMake() {
		return vehicleMake;
	}

	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	
	public Integer getVehicleYear () {
		return vehicleYear;
	}
	
	public void setVehicleYear (Integer vehicleYear) {
		this.vehicleYear = vehicleYear;
	}
    
	public String getBondStartDate () {
		return bondStartDate;
	}
    
	public void setBondStartDate (String bondStartDate) {
		this.bondStartDate = bondStartDate;
	}
	
	public String getBondEndDate () {
		return bondEndDate;
	}
	
	public void setBondEndDate (String bondEndDate) {
		this.bondEndDate = bondEndDate;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
