package com.ms.userServices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserVehicleInfo {

	@Id
	@Column(name = "vehicle_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userVehicleId;

	private Integer bondAmount;
	private Integer bondWeeks;
	private String bondStartDate;
	private String bondEndDate;
	
	@Column(name = "vehicle_make")
	private String vehicleMake;
	
	@Column(name = "vehicle_year")
	private Integer vehicleYear;
	
	private String vehicleModel;
	private String registrationNumber;
	private String fuelType;
	private String note;
	private String vehicleStatus;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private UserInfo user;

	public Long getUserVehicleId() {
		return userVehicleId;
	}
	public void setUserVehicleId(Long userVehicleId) {
		this.userVehicleId = userVehicleId;
	}
	public String getVehicleStatus() {
		return vehicleStatus;
	}
	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public String getBondEndDate() {
		return bondEndDate;
	}
	public void setBondEndDate(String bondEndDate) {
		this.bondEndDate = bondEndDate;
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
	public Integer getVehicleYear() {
		return vehicleYear;
	}
	public void setVehicleYear(Integer vehicleYear) {
		this.vehicleYear = vehicleYear;
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
