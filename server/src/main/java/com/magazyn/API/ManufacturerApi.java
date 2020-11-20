package com.magazyn.API;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String getManufacturersByIdData(@PathVariable int id) {
        return "Manufacturer id = " + id;
    }

    @GetMapping("/api/manufacturer/name/{name}")
    public String getManufacturersByNameData(@PathVariable String name) {
        return "Manufacturer anme = " + name;
    }
}