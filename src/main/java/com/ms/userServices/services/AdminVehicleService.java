package com.ms.userServices.services;

import com.ms.userServices.entity.AdminVehicleInfo;
import com.ms.userServices.model.AddVehicleRequest;
import com.ms.userServices.repository.AdminVehicleInfoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminVehicleService {

    @Autowired
    private AdminVehicleInfoRepository adminVehicleInfoRepository;

    public List<AdminVehicleInfo> getAllVehicles() {
        return adminVehicleInfoRepository.findAll();
    }

    public List<Map<String, Object>> searchVehiclesByRegistrationNumber(String regNumber) {
        List<AdminVehicleInfo> vehicles = adminVehicleInfoRepository
                .findByRegistrationNumberContainingIgnoreCaseAndVehicleStatus(regNumber, "Active");

        return vehicles.stream().map(vehicle -> {
            Map<String, Object> map = new HashMap<>();
            map.put("registrationNumber", vehicle.getRegistrationNumber());
            map.put("vehicleMake", vehicle.getVehicleMake());
            map.put("vehicleModel", vehicle.getVehicleModel());
            map.put("vehicleYear", vehicle.getVehicleYear());
            map.put("fuelType", vehicle.getFuelType());
            return map;
        }).collect(Collectors.toList());
    }

    public ResponseEntity<String> addVehicle(AddVehicleRequest addVehicleRequest) {
        if (adminVehicleInfoRepository.existsByRegistrationNumber(addVehicleRequest.getRegistrationNumber())) {
            return ResponseEntity.badRequest().body("Vehicle with this registration number already exists.");
        }

        AdminVehicleInfo vehicleInfo = new AdminVehicleInfo();
        BeanUtils.copyProperties(addVehicleRequest, vehicleInfo);
        vehicleInfo.setVehicleStatus("Active");

        adminVehicleInfoRepository.save(vehicleInfo);
        return ResponseEntity.ok("Vehicle added successfully");
    }

    public ResponseEntity<String> updateVehicleStatus(String registrationNum, String status) {
        Optional<AdminVehicleInfo> vehicleOpt = adminVehicleInfoRepository.findByRegistrationNumber(registrationNum);

        if (vehicleOpt.isPresent()) {
            AdminVehicleInfo vehicle = vehicleOpt.get();
            vehicle.setVehicleStatus(status);
            adminVehicleInfoRepository.save(vehicle);
            return ResponseEntity.ok("Vehicle status updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Vehicle with registration number " + registrationNum + " not found.");
        }
    }
}
