package com.ms.userServices.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleRequest {
	
	private Integer bondAmount;
	private Integer bondWeeks;
	private String bondStartDate;
	private String bondEndDate;
	private String vehicleMake;
	private Integer vehicleYear;
	private String vehicleModel;
	private String registrationNumber;
	private String fuelType;
	private String note;
	private String vehicleStatus;
	
	public String getVehicleStatus() {
		return vehicleStatus;
	}
	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}
	public Integer getBondAmount() {
		return bondAmount;
	}
	public void setBondAmount(Integer bondAmount) {
		this.bondAmount = bondAmount;
	}
	public Integer getBondWeeks() {
		return bondWeeks;
	}
	public void setBondWeeks(Integer bondWeeks) {
		this.bondWeeks = bondWeeks;
	}
	public String getBondStartDate() {
		return bondStartDate;
	}
	public void setBondStartDate(String bondStartDate) {
		this.bondStartDate = bondStartDate;
	}
	public String getBondEndDate() {
		return bondEndDate;
	}
	public void setBondEndDate(String bondEndDate) {
		this.bondEndDate = bondEndDate;
	}
	public String getVehicleMake() {
		return vehicleMake;
	}
	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}
	public Integer getVehicleYear() {
		return vehicleYear;
	}
	public void setVehicleYear(Integer vehicleYear) {
		this.vehicleYear = vehicleYear;
	}
	public String getVehicleModel() {
		return vehicleModel;
	}
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}
