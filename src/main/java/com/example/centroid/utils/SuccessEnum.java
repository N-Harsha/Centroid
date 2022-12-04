package com.example.centroid.utils;

public enum SuccessEnum {

    USER_REGISTRATION_SUCCESSFUL("User registration successful");
    private final String message;

    SuccessEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
