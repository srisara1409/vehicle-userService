package com.ms.userServices.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String mobileNumber;

    private String emergencyContactName;
    private String emergencyContactNumber;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private String bankName;
    private String accountName;
    private String bsbNumber;
    private String accountNumber;

    private String vehicleType;
    private String licenseNumber;
    private String licenseState;
    private String licenseCountry;
    
	private boolean teamsAndConditions;
	private String status;
	
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

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<UserVehicleInfo> vehicles = new ArrayList<>();
	
	public byte[] getSignature() {
		return signature;
	}
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	public List<UserVehicleInfo> getVehicles() {
		return vehicles;
	}
	public void setVehicles(List<UserVehicleInfo> vehicles) {
		this.vehicles = vehicles;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	public String getLicenseCountry() {
		return licenseCountry;
	}
	public void setLicenseCountry(String licenseCountry) {
		this.licenseCountry = licenseCountry;
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
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
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
	public boolean getTeamsAndConditions() {
		return teamsAndConditions;
	}
	public void setTeamsAndConditions(boolean teamsAndConditions) {
		this.teamsAndConditions = teamsAndConditions;
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
