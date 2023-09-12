package com.oxog.userservice.model.responseModel.userDto;

import com.oxog.userservice.model.responseModel.order.ResponseOrder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserModel {
    private long id;
    private String userSeq;
    private String email;
    private String name;
    private String nickName;
    private String address;
    private String pwd;
    private String userId;
    private Date createdAt;
    private String deleteYn;
    private String encryptedPwd;//복호화된 비번
    private byte[] userIcon; // 유저 아이콘 (이미지 데이터)
    private List<ResponseOrder> orders;
}
