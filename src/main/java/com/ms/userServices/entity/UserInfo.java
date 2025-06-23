package com.ms.userServices.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

	@Id 
	@GeneratedValue
	private Long id;

	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private String mobileNumber;
	private String email;
	private String vehicleType;
	private String licenseNumber;
	private String licenseState;
	private String financialInstName;
	
	@Lob
	@JsonIgnore
	private byte[] licensePhoto;

	@Lob
	@JsonIgnore
	private byte[] passportCopy;

	@Lob
	@JsonIgnore
	private byte[] photoIdCopy;
	
	@Lob
	@JsonIgnore
	private byte[] signature;
	
	@Lob
	@JsonIgnore
	private byte[] bankDetailsPdf;
	
	private String addressLine;
	private String city;
	private String postalCode;
	private String state;
	private String country;
	private boolean checkBox;
	private String accountName;
	private String bsbNumber;
	private String accountNumber;
	private String status;
	private String emergencyContactName;
	private String emergencyContactNumber;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<VehicleDetails> vehicles = new ArrayList<>();
	
	
	public byte[] getSignature() {
		return signature;
	}
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	public List<VehicleDetails> getVehicles() {
		return vehicles;
	}
	public void setVehicles(List<VehicleDetails> vehicles) {
		this.vehicles = vehicles;
	}
	public String getFinancialInstName() {
		return financialInstName;
	}
	public void setFinancialInstName(String financialInstName) {
		this.financialInstName = financialInstName;
	}
	public String getEmergencyContactName() {
		return emergencyContactName;
	}
	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}
	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}
	public void setEmergencyContactNumber(String emergencyContactNumber) {
		this.emergencyContactNumber = emergencyContactNumber;
	}
	public byte[] getBankDetailsPdf() {
		return bankDetailsPdf;
	}
	public void setBankDetailsPdf(byte[] bankDetailsPdf) {
		this.bankDetailsPdf = bankDetailsPdf;
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
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	public String getLicenseState() {
		return licenseState;
	}
	public void setLicenseState(String licenseState) {
		this.licenseState = licenseState;
	}
	public byte[] getLicensePhoto() {
		return licensePhoto;
	}
	public void setLicensePhoto(byte[] licensePhoto) {
		this.licensePhoto = licensePhoto;
	}
	public byte[] getPassportCopy() {
		return passportCopy;
	}
	public void setPassportCopy(byte[] passportCopy) {
		this.passportCopy = passportCopy;
	}
	public byte[] getPhotoIdCopy() {
		return photoIdCopy;
	}
	public void setPhotoIdCopy(byte[] photoIdCopy) {
		this.photoIdCopy = photoIdCopy;
	}
	public String getAddressLine() {
		return addressLine;
	}
	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public boolean getCheckBox() {
		return checkBox;
	}
	public void setCheckBox(boolean checkBox) {
		this.checkBox = checkBox;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBsbNumber() {
		return bsbNumber;
	}
	public void setBsbNumber(String bsbNumber) {
		this.bsbNumber = bsbNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
}
