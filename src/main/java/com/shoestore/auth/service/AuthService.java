package com.shoestore.auth.service;

import com.shoestore.auth.dto.LoginRequestDto;
import com.shoestore.auth.dto.RegisterRequestDto;
import com.shoestore.auth.entity.User;
import com.shoestore.auth.repository.UserRepository;
import com.shoestore.common.enums.Role;
import com.shoestore.config.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    public  void RegisterRequest(RegisterRequestDto requestDto){
        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        User user=User.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(Role.USER)
                .isActive(true)
                .build();
        userRepository.save(user);
    }
    public String login(LoginRequestDto requestDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword()
                )
        );
        return jwtService.generateToken(requestDto.getEmail());
    }
}
