package com.bank.authentication.service;

import com.bank.authentication.dto.AuthenticationTokenDTO;
import com.bank.authentication.request.CustomerLoginDTO;

public interface LoginService {
    AuthenticationTokenDTO login(CustomerLoginDTO loginDTO);
}
