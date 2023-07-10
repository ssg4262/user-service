package com.oxog.userservice.model.responseModel.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oxog.userservice.model.responseModel.order.ResponseOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {
    private String email;
    private String name;
    private String nickName;
    private String address;
    private String userId;

    private List<ResponseOrder> orders;
}
