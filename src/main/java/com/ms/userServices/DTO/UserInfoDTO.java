package com.ms.userServices.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class UserInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String mobileNumber;
    private String email;
    private String vehicleType;
    private String licenseNumber;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<UserVehicleDTO> vehicles;

    // JPQL projection constructor
    public UserInfoDTO(Long id, String firstName, String lastName, String dateOfBirth, String mobileNumber, String email, String vehicleType, String licenseNumber, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.vehicleType = vehicleType;
        this.licenseNumber = licenseNumber;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Full constructor
    public UserInfoDTO(Long id, String firstName, String lastName, String dateOfBirth, String mobileNumber, String email, String vehicleType, String licenseNumber, String status, LocalDateTime createdAt, LocalDateTime updatedAt, List<UserVehicleDTO> vehicles) {
        this(id, firstName, lastName, dateOfBirth, mobileNumber, email, vehicleType, licenseNumber, status, createdAt, updatedAt);
        this.vehicles = vehicles;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	
	public String getLicenseNumber () {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public LocalDateTime getCreatedAt() { 
		return createdAt; 
	}

	public void setCreatedAt(LocalDateTime createdAt) { 
		this.createdAt = createdAt; 
	}
	
    public LocalDateTime getUpdatedAt() { 
    	return updatedAt; 
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) { 
    	this.updatedAt = updatedAt; 
    }
    
	public List<UserVehicleDTO> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<UserVehicleDTO> vehicles) {
		this.vehicles = vehicles;
	}


}
