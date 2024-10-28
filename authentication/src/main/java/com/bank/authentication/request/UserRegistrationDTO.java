package com.bank.authentication.request;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDTO {

    @NotBlank(message = "İsim alanı boş olamaz.")
    private String firstName;

    @NotBlank(message = "Soyisim alanı boş olamaz.")
    private String lastName;

    @NotBlank(message = "E-posta adresi boş olamaz.")
    @Email(message = "Geçersiz e-posta adresi.")
    private String email;

    @NotBlank(message = "Telefon numarası boş olamaz.")
    @Size(min = 11, max = 11, message = "Telefon numarası tam olarak 11 karakter olmalıdır.")
    private String phoneNumber;

    @NotBlank(message = "TC Kimlik Numarası boş olamaz.")
    @Size(min = 11, max = 11, message = "TC Kimlik numarası tam olarak 11 karakter olmalıdır.")
    private String nationalId;

    @NotNull(message = "Doğum tarihi boş olamaz.")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Adres alanı boş olamaz.")
    private String address;

    @NotBlank(message = "Şehir alanı boş olamaz.")
    private String city;

    @NotBlank(message = "Ülke alanı boş olamaz.")
    private String country;

    @NotBlank(message = "Şifre alanı boş olamaz.")
    @Size(min = 6, max = 6, message = "Şifre tam olarak 6 karakter olmalıdır.")
    private String password;


}