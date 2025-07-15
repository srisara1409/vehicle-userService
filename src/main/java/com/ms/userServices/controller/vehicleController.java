package com.ms.userServices.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ms.userServices.entity.UserInfo;
import com.ms.userServices.entity.VehicleDetails;
import com.ms.userServices.entity.VehicleInfo;
import com.ms.userServices.model.AddVehicleRequest;
import com.ms.userServices.model.VehicleRequest;
import com.ms.userServices.repository.UserLoginRepository;
import com.ms.userServices.repository.VehicleInfoRepository;
import com.ms.userServices.services.VehicleService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicle")
public class vehicleController {

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private UserLoginRepository userRepository;

	@Autowired
	private VehicleInfoRepository vehicleInfoRepository;

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

	@GetMapping("/search")
	public ResponseEntity<List<Map<String, Object>>>  searchVehicles(@RequestParam String regNumber) {
		List<VehicleInfo> vehicles = vehicleInfoRepository
				.findByRegistrationNumberContainingIgnoreCaseAndStatus(regNumber, "Active");
		List<Map<String, Object>> summaries = vehicles.stream().map(vehicle -> {
			Map<String, Object> map = new HashMap<>();
			map.put("registrationNumber", vehicle.getRegistrationNumber());
			map.put("make", vehicle.getMake());
			map.put("model", vehicle.getModel());
			map.put("year", vehicle.getYear());
			map.put("fuelType", vehicle.getFuelType());
			return map;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(summaries);
	}

	@GetMapping("/allVehicle")
	public List<VehicleInfo> getAllVehicles() {
		return vehicleInfoRepository.findAll();
	}

	@PostMapping("/addVehicle")
	public ResponseEntity<String> addVehicle(@RequestBody AddVehicleRequest addVehicleRequest) {
		// Optional: check if registration number already exists
		if (vehicleInfoRepository.existsByRegistrationNumber(addVehicleRequest.getRegistrationNumber())) {
			return ResponseEntity.badRequest().body("Vehicle with this registration number already exists.");
		}
		VehicleInfo vehicleInfo = new VehicleInfo();
		BeanUtils.copyProperties(addVehicleRequest, vehicleInfo);
		vehicleInfo.setStatus("Active");
		vehicleInfoRepository.save(vehicleInfo);
		return ResponseEntity.ok("Vehicle added successfully");
	}

	@PatchMapping("/update/{userId}")
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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
	}

	@PutMapping("/updateVehicleStatus/{registrationNumber}")
	public ResponseEntity<String> updateVehicleStatus(
			@PathVariable("registrationNumber") String registrationNum,
			@RequestParam String status) {

		Optional<VehicleInfo> vehicleOpt = vehicleInfoRepository.findByRegistrationNumber(registrationNum);
		if (vehicleOpt.isPresent()) {
			VehicleInfo vehicle = vehicleOpt.get();
			vehicle.setStatus(status);
			vehicleInfoRepository.save(vehicle);
			return ResponseEntity.ok("Vehicle status updated successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Vehicle with registration number " + registrationNum + " not found.");
		}
	}

	@GetMapping("/updateUserClosed/{userId}")
	public ResponseEntity<String> updateUserStatusIfAllVehiclesEnded(@PathVariable("userId") Long userId) {
		Optional<UserInfo> optionalUser = userRepository.findById(userId);

		if (!optionalUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		UserInfo user = optionalUser.get();
		List<VehicleDetails> vehicles = user.getVehicles();

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
