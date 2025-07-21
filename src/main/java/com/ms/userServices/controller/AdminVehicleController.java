// Enhanced AdminVehicleController with Service and Repository

package com.ms.userServices.controller;

import com.ms.userServices.entity.AdminVehicleInfo;
import com.ms.userServices.model.AddVehicleRequest;
import com.ms.userServices.services.AdminVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/adminVehicle")
public class AdminVehicleController {

    @Autowired
    private AdminVehicleService adminVehicleService;

    @GetMapping("/allVehicle")
    public ResponseEntity<List<AdminVehicleInfo>> getAllVehicles() {
        return ResponseEntity.ok(adminVehicleService.getAllVehicles());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchVehicles(@RequestParam String regNumber) {
        return ResponseEntity.ok(adminVehicleService.searchVehiclesByRegistrationNumber(regNumber));
    }

    @PostMapping("/addVehicle")
    public ResponseEntity<String> addVehicle(@RequestBody AddVehicleRequest addVehicleRequest) {
        return adminVehicleService.addVehicle(addVehicleRequest);
    }

    @PutMapping("/updateVehicleStatus/{registrationNumber}")
    public ResponseEntity<String> updateVehicleStatus(@PathVariable String registrationNumber,
                                                      @RequestParam String status) {
        return adminVehicleService.updateVehicleStatus(registrationNumber, status);
    }
}