package com.ms.userServices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ms.userServices.entity.UserInfo;
import com.ms.userServices.repository.UserLoginRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class UserStatusScheduler {

    @Autowired
    private UserLoginRepository userLoginRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Scheduled(cron = "0 0 0 * * *", zone = "Australia/Sydney")  // Every day at 12:00 AM Sydney time
    public void updateClosedUsers() {
        List<UserInfo> users = userLoginRepository.findAll();

        LocalDate today = LocalDate.now();

        for (UserInfo user : users) {
            boolean hasActiveVehicle = user.getVehicles().stream()
                    .anyMatch(vehicle -> {
                        String bondEndDate = vehicle.getBondEndDate();
                        if (bondEndDate == null || bondEndDate.isBlank()) return true;

                        try {
                            LocalDate endDate = LocalDate.parse(bondEndDate, FORMATTER);
                            return !endDate.isBefore(today);  // Still active or future
                        } catch (Exception e) {
                            return true; // If date parsing fails, assume active
                        }
                    });

            if (!hasActiveVehicle && !"CLOSED".equalsIgnoreCase(user.getStatus())) {
                user.setStatus("CLOSED");
                userLoginRepository.save(user);
                System.out.println("User ID " + user.getId() + " status updated to CLOSED");
            }
        }
    }
}

