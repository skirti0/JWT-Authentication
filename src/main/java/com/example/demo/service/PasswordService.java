package com.example.demo.service;


import com.example.demo.dto.ChangePasswordDTO;
import com.example.demo.dto.ForgotPasswordDTO;
import com.example.demo.dto.PasswordResetDTO;

public interface PasswordService {

  void processForgotPassword(ForgotPasswordDTO forgotPassword);

  void resetPassword(PasswordResetDTO passwordReset);

  void changePassword(String username, ChangePasswordDTO changePassword);
}
