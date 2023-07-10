package com.oxog.userservice.model.requestModel;

import lombok.Data;

@Data
public class CreateChatMessageRequest {
    private String message;
    private String userId;
    private String sender;
}
