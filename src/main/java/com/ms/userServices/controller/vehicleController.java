package com.ms.userServices.controller;

import java.util.List;
import java.util.Optional;

import com.ms.userServices.entity.UserInfo;
import com.ms.userServices.model.VehicleRequest;
import com.ms.userServices.services.VehicleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/vehicle")
public class vehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public String testMethod() {
        return "Test successful";
    }

    @GetMapping("/getUser")
    public List<UserInfo> getAllUsers() {
        return vehicleService.getUserDetails();
    }

    @GetMapping("/getUser/{id}")
    public Optional<UserInfo> getUserById(@PathVariable Long id) {
        return vehicleService.getUserById(id);
    }

    @PostMapping("/approve/{userId}")
    public ResponseEntity<String> addVehicle(@PathVariable Long userId, @RequestBody VehicleRequest vehicleRequest) {
        boolean added = vehicleService.saveVehicleToUser(userId, vehicleRequest);
        if (added) {
            return ResponseEntity.ok("Vehicle added successfully");
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }
    
    @PatchMapping("/update/{userId}")
    public ResponseEntity<String> updateOrAddVehicle(
            @PathVariable Long userId,
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
}
