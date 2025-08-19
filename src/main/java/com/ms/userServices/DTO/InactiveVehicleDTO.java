package com.ms.userServices.DTO;

import java.time.LocalDateTime;

public interface InactiveVehicleDTO {
    Long getUserVehicleId();
    String getRegistrationNumber();
    String getVehicleModel();
    String getVehicleMake();
    Integer getVehicleYear();
    Integer getBondAmount();
    Integer getBondWeeks();
    String getBondStartDate();
    String getBondEndDate();
    String getFuelType();
    String getVehicleStatus();
    String getNote();
    LocalDateTime getUpdatedAt();
}
