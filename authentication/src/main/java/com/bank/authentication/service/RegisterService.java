package com.bank.authentication.service;

import com.bank.authentication.request.UserRegistrationDTO;

public interface RegisterService {
    void register(UserRegistrationDTO registerDTO);
}
