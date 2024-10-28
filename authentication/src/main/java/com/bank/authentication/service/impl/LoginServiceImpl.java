package com.bank.authentication.service.impl;

import com.bank.authentication.configuration.JwtService;
import com.bank.authentication.dto.AuthenticationTokenDTO;
import com.bank.authentication.entity.User;
import com.bank.authentication.repository.UserRepository;
import com.bank.authentication.request.CustomerLoginDTO;
import com.bank.authentication.service.LoginService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public AuthenticationTokenDTO login(CustomerLoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getNationalId(),
                        loginDTO.getPassword()
                )
        );
        User user = userRepository.findByNationalId(loginDTO.getNationalId())
                .orElseThrow(() -> new RuntimeException("Hata")); // TODO Add Exception
        String jwtToken = jwtService.generateToken(user);
        log.info("Giris serviceden onaylandi");
        return AuthenticationTokenDTO.builder()
                .token(jwtToken)
                .build();
    }
}
