package com.magazyn.API;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import com.magazyn.database.Manufacturer;
import com.magazyn.database.repositories.ManufacturerRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManufacturerApi {
    @Autowired
    private ManufacturerRepository manufacturer_repository;

    @RequestMapping(value = "/api/manufacturer/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @GetMapping("/api/manufacturer/all/")
    public String getAllManufacturers() {
        Iterable<Manufacturer> all_manufacturers = manufacturer_repository.findAll();

        JSONObject response = new JSONObject();
        JSONArray manufacturers = new JSONArray();

        for (Manufacturer manufacturer : all_manufacturers) {
            JSONObject manufacturer_data = new JSONObject();
            manufacturer_data.put("name", manufacturer.getName());
            manufacturer_data.put("ID", manufacturer.getId());

            manufacturers.put(manufacturer_data);
        }

        response.put("manufacturers", manufacturers);

        return response.toString();
    }

    @GetMapping("/api/manufacturer/id/{id}")
    public String getManufacturerById(@PathVariable int id) {
        Optional<Manufacturer> manufacturer = manufacturer_repository.findById(id);

        if (!manufacturer.isPresent()) {
            throw new NoResourceFoundException();
        }

        JSONObject response = new JSONObject();

        response.put("name", manufacturer.get().getName());
        response.put("ID", manufacturer.get().getId());

        return response.toString();
    }

    @GetMapping("/api/manufacturer/name/{name}")
    public String getManufacturersByName(@PathVariable String name) {
        Iterable<Manufacturer> manufacturers = manufacturer_repository.findByName(name);

        JSONObject response = new JSONObject();
        JSONArray manufacturers_data = new JSONArray();

        for (Manufacturer manufacturer : manufacturers) {
            JSONObject tmanufacturer_data = new JSONObject();
            tmanufacturer_data.put("name", manufacturer.getName());
            tmanufacturer_data.put("ID", manufacturer.getId());

            manufacturers_data.put(tmanufacturer_data);
        }

        response.put("manufacturers", manufacturers_data);

        return response.toString();
    }

    @PutMapping("/api/manufacturer/id/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void setManufacturerById(@PathVariable int id, @RequestParam Map<String, String> allRequestParams) {
        Optional<Manufacturer> manufacturer_opt = manufacturer_repository.findById(id);

        if (!manufacturer_opt.isPresent()) {
            throw new NoResourceFoundException();
        }

        Manufacturer manufacturer = manufacturer_opt.get();

        for (Entry<String, String> param : allRequestParams.entrySet()) {
            switch (param.getKey()) {
                case "name":
                    manufacturer.setName(param.getValue());
                    break;

                default:
                    throw new IllegalRequestException();
            }
        }

        manufacturer_repository.save(manufacturer);
    }

    @PutMapping("/api/manufacturer/add/")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void addManufacturer(@RequestParam Map<String, String> allRequestParams) {
        Manufacturer manufacturer = new Manufacturer();

        // boolean for every field!
        List<Boolean> is_vaid = Arrays.asList(new Boolean[] { false });

        for (Entry<String, String> param : allRequestParams.entrySet()) {
            switch (param.getKey()) {
                case "name":
                    manufacturer.setName(param.getValue());
                    is_vaid.set(0, true);
                    break;

                default:
                    throw new IllegalRequestException();
            }
        }

        if (is_vaid.stream().anyMatch(x -> !x)) {
            // Not all fields are set!
            throw new IllegalRequestException();
        }

        manufacturer_repository.save(manufacturer);
    }

    @DeleteMapping("/api/manufacturer/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delManufacturerById(@PathVariable int id) {
        try {
        manufacturer_repository.deleteById(id);
        } catch (Exception exception) {
            throw new NoResourceFoundException();
        }
    }
}
