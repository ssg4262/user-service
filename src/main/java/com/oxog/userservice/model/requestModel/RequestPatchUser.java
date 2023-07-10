package com.oxog.userservice.model.requestModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestPatchUser {
    @NotNull(message = "Name cannot be null")
    @Size(min = 2 , message = "Name Not be less than 2 characters")
    private String name;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2 , message = "Name Not be less than 2 characters")
    private String nickName;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2 , message = "Name Not be less than 2 characters")
    private String address;
}
