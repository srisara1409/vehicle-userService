package com.ms.userServices.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.userServices.entity.VehicleDetails;
import com.ms.userServices.entity.UserInfo;
import com.ms.userServices.model.VehicleRequest;
import com.ms.userServices.repository.AdminLoginRepository;
import com.ms.userServices.repository.UserLoginRepository;
import com.ms.userServices.repository.VehicleRepository;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepo;

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

            VehicleDetails vehicle = new VehicleDetails();
            BeanUtils.copyProperties(vehicleRequest, vehicle);
            vehicle.setUser(user);
            user.setStatus("APPROVED");
            vehicleRepo.save(vehicle);
            return true;
        }
        return false;
    }

    public boolean addOrUpdateVehicleToUser(Long userId, VehicleRequest vehicleRequest) {
        Optional<UserInfo> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            UserInfo user = userOpt.get();

            // ✅ Check if vehicle with the same registration number already exists globally
            Optional<VehicleDetails> existingVehicle = vehicleRepo.findByRegistrationNumber(vehicleRequest.getRegistrationNumber());
            if (existingVehicle.isPresent() && !existingVehicle.get().getUser().getId().equals(userId)) {
                throw new IllegalStateException("This registration number already exists for another user.");
            }

            // ✅ Check if user's last vehicle has the same registration number
            List<VehicleDetails> existingVehicles = user.getVehicles();
            if (!existingVehicles.isEmpty()) {
                VehicleDetails lastVehicle = existingVehicles.get(existingVehicles.size() - 1);
                if (lastVehicle.getRegistrationNumber().equalsIgnoreCase(vehicleRequest.getRegistrationNumber())) {
                    // 🟡 Update last vehicle
                    BeanUtils.copyProperties(vehicleRequest, lastVehicle);
                    vehicleRepo.save(lastVehicle);
                    return true;
                }
            }

            // ✅ Add new vehicle if different registration number
            VehicleDetails newVehicle = new VehicleDetails();
            BeanUtils.copyProperties(vehicleRequest, newVehicle);
            newVehicle.setUser(user);

            vehicleRepo.save(newVehicle);
            user.getVehicles().add(newVehicle);

            user.setStatus("APPROVED");
            userRepo.save(user);

            return true;
        }
        return false;
    }
}
