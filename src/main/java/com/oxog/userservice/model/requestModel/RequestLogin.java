package com.oxog.userservice.model.requestModel;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestLogin {
    @NotNull(message = "Email Cannot be null")
    @Size(min = 4 , message = "Email not be less than 4")
    @Email
    private String email;

    @NotNull(message = "password Cannot be null")
    @Size(min = 8 , message = "password not be less than 8")
    private String password;
}
