package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.AddressDto;
import com.example.pizza_backend.api.dto.input.AddressInput;
import com.example.pizza_backend.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {

    private AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAddress(HttpServletRequest request) {
        Long profileId = (Long) request.getAttribute("profile_id");
        AddressDto address = addressService.getAddressByProfileId(profileId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("address", address);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAddress(@RequestBody AddressInput addressInput) {
        String createLog = addressService.updateAddress(addressInput);
        if  (createLog == "success") {
            return ResponseEntity.ok().body(Map.of("message", "Update address successfully"));
        }
        return ResponseEntity.badRequest().build();
    }
}
