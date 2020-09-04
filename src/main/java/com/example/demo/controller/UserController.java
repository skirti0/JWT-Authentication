package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;

@RestController
@RequestMapping("/rest/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body("Activation link is send to your mail");
    }

    @PostMapping("/_activate")
    public ResponseEntity<?> registerUser(@RequestParam String token) throws ValidationException {
        String response = userService.activateUser(token);
        return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body(response);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveUsers() {
        return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body(userService.getActiveUsers());
    }

}
