package com.bank.authentication.controller;


import com.bank.authentication.request.CustomerLoginDTO;
import com.bank.authentication.service.LoginService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class LoginController {

   private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody CustomerLoginDTO loginDTO){
        try{
            log.info("Giris islemi basladi");
            return ResponseEntity.ok(loginService.login(loginDTO));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

}
