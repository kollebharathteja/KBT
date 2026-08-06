package com.kbt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email ID or username is required")
    private String emailId; // accepts email OR the special super-admin username "K"

    @NotBlank(message = "Password is required")
    private String password;

    private boolean rememberMe;
}
