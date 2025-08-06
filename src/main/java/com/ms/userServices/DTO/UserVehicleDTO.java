package com.ms.userServices.DTO;

public class UserVehicleDTO {
    private Long userId;
    private Long userVehicleId;
    private String registrationNumber;
    private String vehicleMake;
    private String vehicleModel;

    public UserVehicleDTO(Long userId, Long userVehicleId, String registrationNumber, String vehicleMake, String vehicleModel) {
        this.userId = userId;
        this.userVehicleId = userVehicleId;
        this.registrationNumber = registrationNumber;
        this.vehicleMake = vehicleMake;
        this.vehicleModel = vehicleModel;
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
    
    
}
