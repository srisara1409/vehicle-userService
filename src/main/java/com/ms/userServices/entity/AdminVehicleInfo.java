package com.ms.userServices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "adminVehicleInfo")
public class AdminVehicleInfo {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AdminVehicleId")
    private Long AdminVehicleId;

    @Column(name = "registration_number", unique = true, nullable = false)
    private String registrationNumber;

    private String vehicleModel;
    private String vehicleMake;
    private int vehicleYear;
    private String fuelType;
    private String vehicleType;
    private String vehicleStatus;
     
	public Long getAdminVehicleId() {
		return AdminVehicleId;
	}
	public void setAdminVehicleId(Long adminVehicleId) {
		AdminVehicleId = adminVehicleId;
	}
	public String getVehicleStatus() {
		return vehicleStatus;
	}
	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getVehicleModel() {
		return vehicleModel;
	}
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	public String getVehicleMake() {
		return vehicleMake;
	}
	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}
	public int getVehicleYear() {
		return vehicleYear;
	}
	public void setVehicleYear(int vehicleYear) {
		this.vehicleYear = vehicleYear;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
}
