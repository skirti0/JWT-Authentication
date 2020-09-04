package com.example.demo.service;

import com.example.demo.dto.ChangePasswordDTO;
import com.example.demo.dto.ForgotPasswordDTO;
import com.example.demo.dto.PasswordResetDTO;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Validator;
import org.apache.commons.lang.StringUtils;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.UUID;

@Service
public class PasswordServiceImpl implements PasswordService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordResetTokenRepository passwordResetTokenRepository;
  @Autowired
  private EmailService emailService;

  @Value("${url.forgot-password}")
  private String forgotPasswordUrl;

  @Override
  public void processForgotPassword(ForgotPasswordDTO forgotPassword) {
    if (forgotPassword == null || StringUtils.isBlank(forgotPassword.getEmail())) {
      throw new ValidationException("Invalid request!");
    }

    if (!Validator.validateEmail(forgotPassword.getEmail())) {
      throw new ValidationException("Invalid email address");
    }

    User user = userRepository.findByEmail(forgotPassword.getEmail());
    if (user == null) {
      throw new ObjectNotFoundException("User not found with provided e-mail address.", forgotPassword.getEmail());
    }

    PasswordResetToken token = new PasswordResetToken();
    token.setToken(UUID.randomUUID().toString());
    token.setUser(user);
    token.setExpiryDate(30);
    passwordResetTokenRepository.save(token);

    String content = "Dear Customer, " +
        "<br/>Please click on provided link to change password. " +
        "<br/><a href=\"" + forgotPasswordUrl + token.getToken() + "\">Reset your password</a>" +
        "<br/>" +
        "<br/>Thank You," +
        "<br/>Team";

    emailService.sendMail(user.getEmail(), "Password reset request", content);
  }

  @Override
  public void resetPassword(PasswordResetDTO passwordReset) {
    if (passwordReset == null) {
      throw new ValidationException("Invalid request!");
    }

    String password = passwordReset.getPassword();

    validatePassword(password, passwordReset.getConfirmPassword());

    if (StringUtils.isBlank(passwordReset.getToken())) {
      throw new ValidationException("Could not find password reset token.");
    }

    PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(passwordReset.getToken());

    if (resetToken == null) {
      throw new ObjectNotFoundException("Invalid token", passwordReset.getToken());
    }

    if (resetToken.isExpired()) {
      throw new ValidationException("Token has expired, please request a new password reset.");
    }

    User user = resetToken.getUser();
    validateUser(user);
    updatePassword(user, password);
    passwordResetTokenRepository.delete(resetToken);
  }

  @Override
  public void changePassword(String username, ChangePasswordDTO changePassword) {
    if (StringUtils.isBlank(username)) {
      throw new ValidationException("Invalid request!");
    }

    validatePassword(changePassword.getPassword(), changePassword.getConfirmPassword());

    User user = userRepository.findByEmail(username);
    validateUser(user);

    updatePassword(user, changePassword.getPassword());

    String content = "Dear Customer, " +
        "<br/>Your password has been changed successfully." +
        "<br/>" +
        "<br/>Thank You," +
        "<br/>Team";

    emailService.sendMail(user.getEmail(), "Password changed", content);
  }

  private void updatePassword(User user, String password) {
    String encodedPassword = new BCryptPasswordEncoder().encode(password);
    user.setPassword(encodedPassword);
    userRepository.save(user);
  }

  private void validatePassword(String password, String confirmPassword) {
    if (StringUtils.isBlank(password) || password.length() < 6) {
      throw new ValidationException("Invalid password.");
    }

    if (!password.equals(confirmPassword)) {
      throw new ValidationException("Password and confirm password must be same.");
    }
  }

  private void validateUser(User user) {
    if (user == null) {
      throw new ObjectNotFoundException("User not found!", "");
    }
  }
}
