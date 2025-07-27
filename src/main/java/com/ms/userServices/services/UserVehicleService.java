package com.ms.userServices.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.userServices.entity.UserVehicleInfo;
import com.ms.userServices.entity.UserInfo;
import com.ms.userServices.model.VehicleRequest;
import com.ms.userServices.repository.UserLoginRepository;
import com.ms.userServices.repository.UserVehicleInfoRepository;

@Service
public class UserVehicleService {

	@Autowired
	private UserVehicleInfoRepository userVehicleInfoRepository;

	@Autowired
	private UserLoginRepository userRepo;

	public List<UserInfo> getUserDetails() {
		return userRepo.findAll();
	}

	public Optional<UserInfo> getUserById(Long id) {
	    Optional<UserInfo> userOpt = userRepo.findById(id);
	    userOpt.ifPresent(user -> {
	        List<UserVehicleInfo> activeVehicles = user.getVehicles().stream()
	            .filter(v -> "Active".equalsIgnoreCase(v.getVehicleStatus()))
	            .toList();
	        user.setVehicles(activeVehicles);
	    });
	    return userOpt;
	}
	
//	public Optional<UserInfo> getUserById(Long id) {
//	    Optional<UserInfo> userOpt = userInfoRepository.findById(id);
//	    userOpt.ifPresent(user -> {
//	        List<UserVehicleInfo> activeVehicles = Optional.ofNullable(user.getVehicles())
//	            .orElse(Collections.emptyList())
//	            .stream()
//	            .filter(v -> "Active".equalsIgnoreCase(v.getVehicleStatus()))
//	            .toList();
//	        user.setVehicles(activeVehicles);
//	    });
//	    return userOpt;
//	}

	public boolean saveVehicleToUser(Long userId, VehicleRequest vehicleRequest) {
		Optional<UserInfo> userOpt = userRepo.findById(userId);
		if (userOpt.isPresent()) {
			UserInfo user = userOpt.get();

			UserVehicleInfo vehicle = new UserVehicleInfo();
			BeanUtils.copyProperties(vehicleRequest, vehicle);
			vehicle.setUser(user);
			vehicle.setVehicleStatus("Active");
			user.setStatus("APPROVED");
			userVehicleInfoRepository.save(vehicle);
			return true;
		}
		return false;
	}

	public boolean addOrUpdateVehicleToUser(Long userId, VehicleRequest vehicleRequest) {
		Optional<UserInfo> userOpt = userRepo.findById(userId);
		if (userOpt.isPresent()) {
			UserInfo user = userOpt.get();
			
		    if (vehicleRequest.getRegistrationNumber() == "" || vehicleRequest.getRegistrationNumber() == null ) {
		        if (vehicleRequest.getUserVehicleId() != null) {
		            Optional<UserVehicleInfo> existingEbike = userVehicleInfoRepository.findById(vehicleRequest.getUserVehicleId());
		            if (existingEbike.isPresent()) {
		                UserVehicleInfo ebikeToUpdate = existingEbike.get();
		                BeanUtils.copyProperties(vehicleRequest, ebikeToUpdate);
		                ebikeToUpdate.setUser(user);
		                userVehicleInfoRepository.save(ebikeToUpdate);
		                return true;
		            }
		        }
		    }

			// 🔒 Check for duplicate reg number (used by other user)
			Optional<UserVehicleInfo> existingReg = userVehicleInfoRepository.findByRegistrationNumber(vehicleRequest.getRegistrationNumber());
			if (existingReg.isPresent() && existingReg.get().getVehicleStatus().equalsIgnoreCase("Active") && !existingReg.get().getUser().getId().equals(userId)) {
				throw new IllegalStateException("This registration number already exists for another user.");
			}

			// ✅ If no matching vehicle ID, and status is Active, enforce limit
			long activeVehicleCount = user.getVehicles().stream()
					.filter(v -> "Active".equalsIgnoreCase(v.getVehicleStatus()))
					.count();

			Optional<UserVehicleInfo> existingForSameUser = user.getVehicles().stream()
					.filter(v -> v.getRegistrationNumber().equalsIgnoreCase(vehicleRequest.getRegistrationNumber()))
					.findFirst();

			if ("Active".equalsIgnoreCase(vehicleRequest.getVehicleStatus()) && activeVehicleCount >= 2 && existingForSameUser.isEmpty()) {
				throw new IllegalStateException("Only 2 vehicles can be Active at a time. Please mark one existing vehicle as Inactive before adding a new Active vehicle.");
			}

			if (existingForSameUser.isPresent()) {
				UserVehicleInfo vehicleToUpdate = existingForSameUser.get();
				BeanUtils.copyProperties(vehicleRequest, vehicleToUpdate);
				vehicleToUpdate.setUser(user);
				userVehicleInfoRepository.save(vehicleToUpdate);
				return true;
			}

			// ✅ Create new if truly a new vehicle
			UserVehicleInfo newVehicle = new UserVehicleInfo();
			BeanUtils.copyProperties(vehicleRequest, newVehicle);
			newVehicle.setUser(user);
			newVehicle.setUserVehicleId(null);
			userVehicleInfoRepository.save(newVehicle);
			// user.getVehicles().add(newVehicle);
			//user.setStatus("APPROVED");
			// userRepo.save(user);

			return true;
		}

		return false;
	}


}
