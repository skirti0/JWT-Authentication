package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.demo.util.Constants.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Value("${activation.url}")
    private String url;

    @Override
    @Transactional
    public void registerUser(UserDTO userDTO) {
        User user = new User(userDTO);
        user.setEnabled(false);
        user.setConfirmationToken(UUID.randomUUID().toString());
        user.setExpriyDate(user.calculateExpiryDate(60 * 24));
        userRepository.save(user);

        emailService.sendMail(user.getEmail(), REGISTRATION_CONFIRMATION, MAIL_BODY
                +"<a href=\"" + url + user.getConfirmationToken() + "\">Activate User</a>");
    }

    @Override
    @Transactional
    public String activateUser(String token) throws ValidationException {
        User user = userRepository.findByConfirmationToken(token);
        if(null == user){
          throw new ValidationException(INVALID_LINK);
        }else {
            Calendar cal = Calendar.getInstance();
            if((user.getExpriyDate().getTime() - cal.getTime().getTime()) <= 0) {
                throw new ValidationException(LINK_EXPIRED);
            }else {
                user.setEnabled(true);
                userRepository.save(user);
            }
        }
        return USER_ACTIVATED;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getActiveUsers() {
       return userRepository.findByEnabledIsTrue().stream()
               .map(UserDTO::new)
               .collect(Collectors.toList());
    }
}
