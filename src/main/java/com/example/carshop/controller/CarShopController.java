package com.example.carshop.controller;

import com.example.carshop.entity.Customer;
import com.example.carshop.entity.Vehicle;
import com.example.carshop.dto.CustomerDTO;
import com.example.carshop.dto.VehicleBrandDTO;
import com.example.carshop.dto.VehicleDTO;
import com.example.carshop.repository.CustomerRepository;
import com.example.carshop.repository.VehicleRepository;
import com.example.carshop.projection.VehicleBrandProjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CarShopController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
        return new VehicleDTO(
                vehicle.getId(),
                vehicle.getRegistrationNumber(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getProductionYear());
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) {

        List<VehicleDTO> vehicles = customer.getVehicles()
                .stream()
                .map(this::convertToVehicleDTO)
                .collect(Collectors.toList());

        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getPhoneNumber(),
                vehicles);
    }

    private VehicleBrandDTO convertToVehicleBrandDTO(
            VehicleBrandProjection projection) {

        return new VehicleBrandDTO(
                projection.getRegistrationNumber(),
                projection.getBrand());
    }

    @PostMapping("/customers")
    public ResponseEntity<String> createCustomer(
            @RequestParam String name,
            @RequestParam String phone) {

        Customer customer = new Customer(name, phone);

        customerRepository.save(customer);

        return ResponseEntity.ok("Customer created successfully");
    }

    @PostMapping("/vehicles")
    public ResponseEntity<String> createVehicle(
            @RequestParam String registrationNumber,
            @RequestParam String brand,
            @RequestParam String model,
            @RequestParam int productionYear) {

        Vehicle vehicle = new Vehicle(registrationNumber, brand, model, productionYear);

        vehicleRepository.save(vehicle);

        return ResponseEntity.ok("Vehicle created successfully");
    }

    @PostMapping("/addvehicletocustomer")
    public ResponseEntity<String> addVehicleToCustomer(
            @RequestParam Long customerId,
            @RequestParam Long vehicleId) {

        Optional<Customer> customer = customerRepository.findById(customerId);

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);

        if (customer.isEmpty() || vehicle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Customer foundCustomer = customer.get();
        Vehicle foundVehicle = vehicle.get();

        foundVehicle.setCustomer(foundCustomer);

        if (!foundCustomer.getVehicles().contains(foundVehicle)) {
            foundCustomer.getVehicles().add(foundVehicle);
        }

        vehicleRepository.save(foundVehicle);

        return ResponseEntity.ok("Vehicle connected to customer");
    }

    @GetMapping("/customers")
    public List<CustomerDTO> getAllCustomersWithVehicles() {

        return customerRepository.findAllCustomersWithVehicles()
                .stream()
                .map(this::convertToCustomerDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/vehicles")
    public List<VehicleDTO> getAllVehicles() {

        return vehicleRepository.findAll()
                .stream()
                .map(this::convertToVehicleDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/vehicles-by-brand")
    public List<VehicleDTO> getVehiclesByBrand(
            @RequestParam String brand) {

        return vehicleRepository.findByBrand(brand)
                .stream()
                .map(this::convertToVehicleDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer-id")
    public ResponseEntity<Long> getCustomerId(
            @RequestParam String name) {

        Optional<Customer> customer = customerRepository.findByName(name);

        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get().getId());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/vehicles/{id}/brand")
    public ResponseEntity<String> updateVehicleBrand(
            @PathVariable Long id,
            @RequestParam String brand) {

        int updatedRows = vehicleRepository.updateVehicleBrand(id, brand);

        if (updatedRows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Vehicle brand updated successfully");
    }

    @GetMapping("/vehicle-brand-info")
    public List<VehicleBrandDTO> getVehicleBrandInfo() {

        return vehicleRepository.findVehicleBrandInfo()
                .stream()
                .map(this::convertToVehicleBrandDTO)
                .collect(Collectors.toList());
    }

}