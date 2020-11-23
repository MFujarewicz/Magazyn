package com.magazyn.API;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManufacturerApi {
    
    @GetMapping("/api/manufacturer/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @GetMapping("/api/manufacturer/all/")
    public String getAllManufacturersData() {
        return "All";
    }

    @GetMapping("/api/manufacturer/id/{id}")
    public String getManufacturersDataById(@PathVariable int id) {
        return "Manufacturer id = " + id;
    }

    @GetMapping("/api/manufacturer/name/{name}")
    public String getManufacturersDataByName(@PathVariable String name) {
        return "Manufacturer name = " + name;
    }

    @DeleteMapping("/api/manufacturer/id/{id}")
    public String delManufacturerById(@PathVariable int id) {
        return "Delete manyfacturer id = " + id;
    }

    @PutMapping("/api/manufacturer/add/")
    public String addManufacturer(int id) {
        return "Delete manyfacturer id = " + id;
    }
}
