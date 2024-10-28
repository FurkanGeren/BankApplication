package com.bank.authentication.controller;


import com.bank.authentication.request.UserRegistrationDTO;
import com.bank.authentication.service.RegisterService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class RegisterController {


    private final RegisterService registerService;


    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO registerDTO) {
        try{
            log.info("Kayit islemi basladi");
            registerService.register(registerDTO);
            return ResponseEntity.ok("Kayıt başarılı.");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }


}
