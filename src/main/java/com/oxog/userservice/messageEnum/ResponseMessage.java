package com.oxog.userservice.messageEnum;

public enum ResponseMessage {
    SUCCESS("Success"),
    ERROR("Error"),
    NOT_FOUND("Not Found"),
    UNAUTHORIZED("Unauthorized");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
