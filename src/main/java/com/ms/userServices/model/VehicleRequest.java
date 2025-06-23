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
	private String make;
	private Integer year;
	private String Model;
	private String registrationNumber;
	private String fuelType;
	private String note;
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
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getModel() {
		return Model;
	}
	public void setModel(String model) {
		Model = model;
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
