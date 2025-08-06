package com.ms.userServices.DTO;


import java.util.List;

public class UserInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String status;
    private List<UserVehicleDTO> vehicles;

    // JPQL projection constructor
    public UserInfoDTO(Long id, String firstName, String lastName, String email, String mobileNumber, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.status = status;
    }

    // Full constructor
    public UserInfoDTO(Long id, String firstName, String lastName, String email, String mobileNumber, String status, List<UserVehicleDTO> vehicles) {
        this(id, firstName, lastName, email, mobileNumber, status);
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<UserVehicleDTO> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<UserVehicleDTO> vehicles) {
		this.vehicles = vehicles;
	}


}
