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
        return userRepo.findById(id);
    }

    public boolean saveVehicleToUser(Long userId, VehicleRequest vehicleRequest) {
        Optional<UserInfo> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            UserInfo user = userOpt.get();

            UserVehicleInfo vehicle = new UserVehicleInfo();
            BeanUtils.copyProperties(vehicleRequest, vehicle);
            vehicle.setUser(user);
            //vehicle.setVehicleStatus("Active");
            user.setStatus("APPROVED");
            userVehicleInfoRepository.save(vehicle);
            return true;
        }
        return false;
    }
    


//    public boolean addOrUpdateVehicleToUser(Long userId, VehicleRequest vehicleRequest) {
//        Optional<UserInfo> userOpt = userRepo.findById(userId);
//        if (userOpt.isEmpty()) return false;
//
//        UserInfo user = userOpt.get();
//        // Check for duplicate registration number used by another user
//        Optional<UserVehicleInfo> existingByReg = userVehicleInfoRepository.findByRegistrationNumber(vehicleRequest.getRegistrationNumber());
//        if (existingByReg.isPresent() && !existingByReg.get().getUser().getId().equals(userId)) {
//            throw new IllegalStateException("This registration number already exists for another user.");
//        }
//        List<UserVehicleInfo> vehicles = user.getVehicles();
//
//        // Find if the vehicle already exists by ID
//        UserVehicleInfo vehicleToUpdate = null;
//        for (UserVehicleInfo v : vehicles) {
//            if (vehicleRequest.getUserVehicleId() != null &&
//                v.getUserVehicleId().equals(vehicleRequest.getUserVehicleId())) {
//                vehicleToUpdate = v;
//                break;
//            }
//        }
//
//        // Count current Active vehicles
//        long activeCount = vehicles.stream()
//            .filter(v -> "Active".equalsIgnoreCase(v.getVehicleStatus()))
//            .count();
//
//        String requestedStatus = vehicleRequest.getVehicleStatus();
//        boolean requestingActive = "Active".equalsIgnoreCase(requestedStatus);
//
//        // Case 1: New registration number → Add as new
//        if (vehicleToUpdate != null &&
//            !vehicleToUpdate.getRegistrationNumber().equalsIgnoreCase(vehicleRequest.getRegistrationNumber())) {
//            vehicleToUpdate = null; // treat as new
//        }
//
//        // Enforce max 2 active rule
//        boolean isNew = (vehicleToUpdate == null);
//        boolean wasInactive = vehicleToUpdate == null || !"Active".equalsIgnoreCase(vehicleToUpdate.getVehicleStatus());
//
//        if (requestingActive && wasInactive && activeCount >= 2) {
//            throw new IllegalStateException("Only 2 vehicles can be Active at a time for a user.");
//        }
//
//        if (vehicleToUpdate != null) {
//            // Update existing vehicle
//            BeanUtils.copyProperties(vehicleRequest, vehicleToUpdate, "userVehicleId", "user");
//            vehicleToUpdate.setUser(user);
//            userVehicleInfoRepository.save(vehicleToUpdate);
//        } else {
//            // Add as new vehicle
//            UserVehicleInfo newVehicle = new UserVehicleInfo();
//            BeanUtils.copyProperties(vehicleRequest, newVehicle);
//            newVehicle.setUser(user);
//            userVehicleInfoRepository.save(newVehicle);
//            user.getVehicles().add(newVehicle);
//        }
//
//        // Set user status
//        user.setStatus("APPROVED");
//        userRepo.save(user);
//        return true;
//    }
    
    public boolean addOrUpdateVehicleToUser(Long userId, VehicleRequest vehicleRequest) {
        Optional<UserInfo> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            UserInfo user = userOpt.get();

            // ✅ Check if vehicle with the same registration number already exists globally
            Optional<UserVehicleInfo> existingVehicle = userVehicleInfoRepository.findByRegistrationNumber(vehicleRequest.getRegistrationNumber());
            if (existingVehicle.isPresent() && !existingVehicle.get().getUser().getId().equals(userId)) {
                throw new IllegalStateException("This registration number already exists for another user.");
            }

            // ✅ Count number of active vehicles for this user
            long activeVehicleCount = user.getVehicles().stream()
                .filter(v -> "Active".equalsIgnoreCase(v.getVehicleStatus()))
                .count();

            // ✅ If trying to add a new Active vehicle, enforce limit
            boolean isAddingNew = true;
            if (!user.getVehicles().isEmpty()) {
                UserVehicleInfo lastVehicle = user.getVehicles().get(user.getVehicles().size() - 1);
                if (lastVehicle.getRegistrationNumber().equalsIgnoreCase(vehicleRequest.getRegistrationNumber())) {
                    isAddingNew = false;
                    // 🟡 Update last vehicle
                    BeanUtils.copyProperties(vehicleRequest, lastVehicle);
                    userVehicleInfoRepository.save(lastVehicle);
                    return true;
                }
            }

            if (isAddingNew && "Active".equalsIgnoreCase(vehicleRequest.getVehicleStatus()) && activeVehicleCount >= 2) {
                throw new IllegalStateException("Only 2 vehicles can be Active at a time. Please mark one existing vehicle as Inactive before adding a new Active vehicle.");
            }

            // ✅ Add new vehicle if different registration number
            UserVehicleInfo newVehicle = new UserVehicleInfo();
            BeanUtils.copyProperties(vehicleRequest, newVehicle);
            newVehicle.setUser(user);

            userVehicleInfoRepository.save(newVehicle);
            user.getVehicles().add(newVehicle);

            user.setStatus("APPROVED");
            userRepo.save(user);

            return true;
        }
        return false;
    }



}
