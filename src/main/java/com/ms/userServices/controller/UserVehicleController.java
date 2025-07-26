package com.ms.userServices.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ms.userServices.entity.UserInfo;
import com.ms.userServices.entity.UserVehicleInfo;
import com.ms.userServices.DTO.InactiveVehicleDTO;
import com.ms.userServices.entity.AdminVehicleInfo;
import com.ms.userServices.model.AddVehicleRequest;
import com.ms.userServices.model.VehicleRequest;
import com.ms.userServices.repository.UserLoginRepository;
import com.ms.userServices.repository.UserVehicleInfoRepository;
import com.ms.userServices.repository.AdminVehicleInfoRepository;
import com.ms.userServices.services.UserVehicleService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/userVehicle")
public class UserVehicleController {

	@Autowired
	private UserVehicleService vehicleService;

	@Autowired
	private UserLoginRepository userRepository;
	
	@Autowired
	private UserVehicleInfoRepository userVehicleInfoRepository;


	@GetMapping
	public String testMethod() {
		return "Test successful";
	}

	@GetMapping("/getUser")
	public List<UserInfo> getAllUsers() {
		return vehicleService.getUserDetails();
	}

	@GetMapping("/getUser/{id}")
	public Optional<UserInfo> getUserById(@PathVariable("id") Long id) {
		return vehicleService.getUserById(id);
	}

	@PostMapping("/approve/{userId}")
	public ResponseEntity<String> addVehicle(@PathVariable("userId") Long userId, @RequestBody VehicleRequest vehicleRequest) {
		boolean added = vehicleService.saveVehicleToUser(userId, vehicleRequest);
		if (added) {
			return ResponseEntity.ok("Vehicle added successfully");
		} else {
			return ResponseEntity.badRequest().body("User not found");
		}
	}

	@PatchMapping("/updateVehicleInfoToUser/{userId}")
	public ResponseEntity<String> updateOrAddVehicle(
			@PathVariable("userId") Long userId,
			@RequestBody VehicleRequest vehicleRequest) {
		try {
			boolean success = vehicleService.addOrUpdateVehicleToUser(userId, vehicleRequest);
			if (success) {
				return ResponseEntity.ok("Vehicle details updated successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
			}
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // Duplicate reg number
		} catch (Exception e) {
			e.printStackTrace(); 
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Not able to modified the record, Please check the updated field.");
		}
	}

	@GetMapping("/inactiveVehicles/{userId}")
	public ResponseEntity<List<InactiveVehicleDTO>> getInactiveVehiclesByUser(@PathVariable Long userId) {
	    return ResponseEntity.ok(userVehicleInfoRepository.findInactiveVehiclesByUserId(userId));
	}
	
	@PatchMapping("/updateUserVehicleStatus/{userVehicleId}")
	public ResponseEntity<String> updateVehicleStatus(
	        @PathVariable Long userVehicleId,
	        @RequestBody Map<String, String> payload) {

	    try {
	        String newStatus = payload.get("vehicleStatus");
	        if (newStatus == null || (!newStatus.equalsIgnoreCase("Active") && !newStatus.equalsIgnoreCase("InActive"))) {
	            return ResponseEntity.badRequest().body("Invalid vehicle status");
	        }

	        Optional<UserVehicleInfo> vehicleOpt = userVehicleInfoRepository.findById(userVehicleId);
	        if (vehicleOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
	        }

	        UserVehicleInfo vehicle = vehicleOpt.get();
	        Long userId = vehicle.getUser().getId(); // assumes vehicle has a User object

	        // ✅ Only check when trying to mark a vehicle as Active
	        if (newStatus.equalsIgnoreCase("Active")) {
	            List<UserVehicleInfo> activeVehicles = userVehicleInfoRepository.findByUserIdAndVehicleStatusIgnoreCase(userId, "Active");

	            // Exclude the current vehicle from count (if it’s currently inactive)
	            boolean alreadyActive = vehicle.getVehicleStatus().equalsIgnoreCase("Active");
	            int activeCount = (int) activeVehicles.stream()
	                    .filter(v -> !v.getUserVehicleId().equals(userVehicleId))
	                    .count();

	            if (!alreadyActive && activeCount >= 2) {
	                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already has 2 active vehicles. Please mark one as Inactive first.");
	            }
	        }

	        // ✅ Update and save
	        vehicle.setVehicleStatus(newStatus);
	        userVehicleInfoRepository.save(vehicle);
	        return ResponseEntity.ok("Vehicle status updated successfully");

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
	    }
	}

	
	@GetMapping("/updateUserClosed/{userId}")
	public ResponseEntity<String> updateUserStatusIfAllVehiclesEnded(@PathVariable("userId") Long userId) {
		Optional<UserInfo> optionalUser = userRepository.findById(userId);

		if (!optionalUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		UserInfo user = optionalUser.get();
		List<UserVehicleInfo> vehicles = user.getVehicles();

		LocalDate today = LocalDate.now();

		boolean hasActiveVehicle = vehicles.stream()
				.anyMatch(vehicle -> {
					LocalDate endDate = parseDate(vehicle.getBondEndDate());
					return endDate != null && !endDate.isBefore(today);
				});

		if (!hasActiveVehicle) {
			user.setStatus("CLOSED");
			userRepository.save(user);
			return ResponseEntity.ok("User status updated to CLOSED");
		}

		return ResponseEntity.ok("User has active vehicle(s); status not updated");
	}

	private LocalDate parseDate(String dateStr) {
		try {
			return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (Exception e) {
			return null;
		}
	}


}
