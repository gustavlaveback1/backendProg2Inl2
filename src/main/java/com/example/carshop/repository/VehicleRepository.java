package com.example.carshop.repository;

import com.example.carshop.entity.Vehicle;
import com.example.carshop.projection.VehicleBrandProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByBrand(String brand);

    @Query("SELECT v FROM Vehicle v WHERE v.customer.id = :customerId")
    List<Vehicle> findVehiclesByCustomerId(
            @Param("customerId") Long customerId);

    // Update the brand for a vehicle
    @Modifying
    @Transactional
    @Query("UPDATE Vehicle v SET v.brand = :brand WHERE v.id = :vehicleId")
    int updateVehicleBrand(
            @Param("vehicleId") Long vehicleId,
            @Param("brand") String brand);

    @Query("SELECT v.registrationNumber AS registrationNumber, " +
            "v.brand AS brand FROM Vehicle v")
    List<VehicleBrandProjection> findVehicleBrandInfo();

}
