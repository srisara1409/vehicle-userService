package com.ms.userServices.DTO;

public interface InactiveVehicleDTO {
    Long getUserVehicleId();
    String getRegistrationNumber();
    String getVehicleModel();
    String getVehicleMake();
    Integer getVehicleYear();
    String getBondStartDate();
    String getBondEndDate();
    String getFuelType();
    String getVehicleStatus();
    String getNote();
}
