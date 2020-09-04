package com.example.demo.service;

import com.example.demo.dto.UserDTO;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface UserService {

    void registerUser(UserDTO userDTO);

    String activateUser(String token) throws ValidationException;

    List<UserDTO> getActiveUsers();
}
