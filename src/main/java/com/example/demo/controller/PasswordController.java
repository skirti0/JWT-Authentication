package com.example.demo.controller;

import com.example.demo.dto.ChangePasswordDTO;
import com.example.demo.dto.ForgotPasswordDTO;
import com.example.demo.dto.PasswordResetDTO;
import com.example.demo.service.PasswordService;
import com.example.demo.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rest/v1")
public class PasswordController {

  @Autowired
  private PasswordService passwordService;

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO request) {
    passwordService.processForgotPassword(request);
    return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body("Success");
  }

  @PutMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDTO request) {
    passwordService.resetPassword(request);
    return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body("Success");
  }

  @PutMapping("/change-password")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO request) {
    String username = SecurityUtils.getCurrentUserLogin();
    passwordService.changePassword(username, request);
    return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body("Success");
  }
}
