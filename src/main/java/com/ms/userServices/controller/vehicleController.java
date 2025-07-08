package com.ms.userServices.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.ms.userServices.entity.UserInfo;
import com.ms.userServices.entity.VehicleDetails;
import com.ms.userServices.entity.VehicleInfo;
import com.ms.userServices.model.VehicleRequest;
import com.ms.userServices.repository.UserLoginRepository;
import com.ms.userServices.repository.VehicleInfoRepository;
import com.ms.userServices.services.VehicleService;

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
    public ResponseEntity<List<VehicleInfo>> searchVehicles(@RequestParam String regNumber) {
        List<VehicleInfo> vehicles = vehicleInfoRepository
            .findByRegistrationNumberContainingIgnoreCaseAndStatus(regNumber, "Active");
        return ResponseEntity.ok(vehicles);
    }
    
    @PostMapping("/addVehicle")
    public ResponseEntity<String> addVehicle(@RequestBody VehicleInfo vehicleInfo) {
        // Optional: check if registration number already exists
        if (vehicleInfoRepository.existsByRegistrationNumber(vehicleInfo.getRegistrationNumber())) {
            return ResponseEntity.badRequest().body("Vehicle with this registration number already exists.");
        }
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
