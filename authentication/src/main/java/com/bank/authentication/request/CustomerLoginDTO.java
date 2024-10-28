package com.bank.authentication.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLoginDTO {

    @NotBlank(message = "TC Kimlik Numarası boş olamaz.")
    @Size(min = 11, max = 11, message = "TC Kimlik numarası tam olarak 11 karakter olmalıdır.")
    private String nationalId;

    @NotBlank(message = "Şifre alanı boş olamaz.")
    private String password;
}
