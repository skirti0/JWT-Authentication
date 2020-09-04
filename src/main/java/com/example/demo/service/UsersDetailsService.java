package com.example.demo.service;

import com.example.demo.configuration.securties.CustomUserDetails;
import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.configuration.securties.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.xml.bind.ValidationException;

import static com.example.demo.util.Constants.*;

@Service
@Transactional
public class UsersDetailsService implements UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND + email);
        }

        return new CustomUserDetails(user);
}

    public AuthenticationResponse getUserDetails(AuthenticationRequest authenticationRequest) throws Exception {
        User user = userDao.findByEmail(authenticationRequest.getEmail());
        if(!user.isEnabled()){
            throw new ValidationException(INVALID_USER);
        }
        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception(INCORRECT_CREDENTIALS);
        }

        final UserDetails userDetails = loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtUtils.generateToken(userDetails, authentication);

        return new AuthenticationResponse(token, "Bearer ", jwtUtils.extractExpiration(token));
    }


}
