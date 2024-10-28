package com.bank.authentication.service.impl;

import com.bank.authentication.entity.User;
import com.bank.authentication.enumrated.AccountStatus;
import com.bank.authentication.enumrated.Role;
import com.bank.authentication.repository.UserRepository;
import com.bank.authentication.request.UserRegistrationDTO;
import com.bank.authentication.service.RegisterService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Random;

@Service
@Log4j2
public class RegisterServiceImpl implements RegisterService {

    @Value("${code.country}")
    private String countryCode;

    @Value("${code.bank}")
    private String bankCode;

    @Value("${account.number.length}")
    private int numberLength;



    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(UserRegistrationDTO registerDTO) {
        User user = User.builder()
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .email(registerDTO.getEmail())
                .phoneNumber(registerDTO.getPhoneNumber())
                .nationalId(registerDTO.getNationalId())
                .dateOfBirth(registerDTO.getDateOfBirth())
                .address(registerDTO.getAddress())
                .city(registerDTO.getCity())
                .country(registerDTO.getCountry())
                .iban(generateIban())
                .customerPassword(passwordEncoder.encode(registerDTO.getPassword()))
                .updatedAt(null)
                .balance(0.0)
                .accountStatus(AccountStatus.ACTIVE)
                .roles(Collections.singleton(Role.CUSTOMER))
                .build();
        userRepository.save(user);
    }

    private String generateIban() {
        StringBuilder iban = new StringBuilder(countryCode);
        iban.append(bankCode);
        do {
            iban = new StringBuilder(countryCode);
            iban.append(bankCode);

            // Rastgele hesap numarası oluşturma
            Random random = new Random();
            for (int i = 0; i < numberLength; i++) {
                iban.append(random.nextInt(10)); // 0-9 arası rastgele sayı eklenir
            }
        } while (userRepository.existsByIban(iban.toString())); // Eğer IBAN daha önce oluşturulmuşsa tekrar dene



        return iban.toString();
    }
}
