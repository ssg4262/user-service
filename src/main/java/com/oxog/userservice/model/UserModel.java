package com.oxog.userservice.model;

import com.oxog.userservice.model.responseModel.order.ResponseOrder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserModel {
    private String email;
    private String name;
    private String address;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String encryptedPwd;//복호화된 비번

    private List<ResponseOrder> orders;
}
