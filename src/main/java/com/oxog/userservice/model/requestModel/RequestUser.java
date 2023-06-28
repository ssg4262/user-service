package com.oxog.userservice.model.requestModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestUser {
    @NotNull(message = "Email cannot be null")
    @Size(min = 2 , message = "Email Not be less than 2 characters")
    private String email;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2 , message = "Name Not be less than 2 characters")
    private String name;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2 , message = "Name Not be less than 2 characters")
    private String address;


    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be equal or grater than 8 characters")
    private String pwd;
}
