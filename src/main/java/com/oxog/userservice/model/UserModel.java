package com.oxog.userservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserModel {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String encryptedPwd;//복호화된 비번
}
