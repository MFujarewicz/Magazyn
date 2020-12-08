package com.magazyn.API;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.Map;
import java.util.Map.Entry;

import com.magazyn.API.exceptions.IllegalRequestException;
import com.magazyn.API.exceptions.NoEndPointException;
import com.magazyn.API.exceptions.NoResourceFoundException;
import com.magazyn.database.Type;
import com.magazyn.database.repositories.TypeRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TypeApi {
    @Autowired
    private TypeRepository type_repository;

    @RequestMapping(value = "/api/type/**")
    public String showError() {
        throw new NoEndPointException();
    }

    @GetMapping("/api/type/all/")
    public String getAllTypes() {
        Iterable<Type> all_types = type_repository.findAll();

        JSONObject response = new JSONObject();
        JSONArray types = new JSONArray();

        for (Type type : all_types) {
            JSONObject type_data = new JSONObject();
            type_data.put("name", type.getName());
            type_data.put("ID", type.getId());

            types.put(type_data);
        }

        response.put("types", types);

        return response.toString();
    }

    @GetMapping("/api/type/id/{id}")
    public String getTypeById(@PathVariable int id) {
        Optional<Type> type = type_repository.findById(id);

        if (!type.isPresent()) {
            throw new NoResourceFoundException();
        }

        JSONObject response = new JSONObject();

        response.put("name", type.get().getName());
        response.put("ID", type.get().getId());

        return response.toString();
    }

    @GetMapping("/api/type/name/{name}")
    public String getTypesByName(@PathVariable String name) {
        Iterable<Type> types = type_repository.findByName(name);

        JSONObject response = new JSONObject();
        JSONArray types_data = new JSONArray();

        for (Type type : types) {
            JSONObject type_data = new JSONObject();
            type_data.put("name", type.getName());
            type_data.put("ID", type.getId());

            types_data.put(type_data);
        }

        response.put("types", types_data);

        return response.toString();
    }

    @PutMapping("/api/type/id/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void setTypeById(@PathVariable int id, @RequestParam Map<String, String> allRequestParams) {
        Optional<Type> type_opt = type_repository.findById(id);

        if (!type_opt.isPresent()) {
            throw new NoResourceFoundException();
        }

        Type type = type_opt.get();

        for (Entry<String, String> param : allRequestParams.entrySet()) {
            switch (param.getKey()) {
                case "name":
                    type.setName(param.getValue());
                    break;

                default:
                    throw new IllegalRequestException();
            }
        }

        type_repository.save(type);
    }

    @PutMapping("/api/type/add/")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void addType(@RequestParam Map<String, String> allRequestParams) {
        Type type = new Type();

        // boolean for every field!
        List<Boolean> is_vaid = Arrays.asList(new Boolean[] { false });

        for (Entry<String, String> param : allRequestParams.entrySet()) {
            switch (param.getKey()) {
                case "name":
                    type.setName(param.getValue());
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

        type_repository.save(type);
    }

    @DeleteMapping("/api/type/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delTypeById(@PathVariable int id) {
        try {
        type_repository.deleteById(id);
        } catch (Exception exception) {
            throw new NoResourceFoundException();
        }
    }
}
