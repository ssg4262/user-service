package com.oxog.userservice.model.requestModel.chat;

import lombok.Data;

@Data
public class ChatUser {
    private String userId;
    private String nickName;
    private String content;
}
