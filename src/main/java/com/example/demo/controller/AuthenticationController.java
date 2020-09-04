package com.example.demo.controller;

import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.service.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/v1/authenticate")
public class AuthenticationController {

    @Autowired
    private UsersDetailsService usersDetailsService;

    @PostMapping
    public ResponseEntity<?> createToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        AuthenticationResponse response = usersDetailsService.getUserDetails(authenticationRequest);
        return ResponseEntity.ok(response);
    }

}
