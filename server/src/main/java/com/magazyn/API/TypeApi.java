package com.magazyn.API;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TypeApi {
    
    @GetMapping("/api/type/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @GetMapping("/api/type/all/")
    public String getAllTypessData() {
        return "All";
    }

    @GetMapping("/api/type/id/{id}")
    public String getTypesByIdData(@PathVariable int id) {
        return "Type id = " + id;
    }

    @GetMapping("/api/type/name/{name}")
    public String getTypesByNameData(@PathVariable String name) {
        return "Type anme = " + name;
    }
}
