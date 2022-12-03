package com.example.centroid.utils;

public enum ErrorEnum {
    USER_SESSION_EXPIRED("Session expired, sign in again", "ERR102"),

    BAD_CREDENTIALS("Bad credentials, please try again", "ERR149"),
    USER_SESSION_CREATION_FAILED("Unable to create user session while logging", "ERR107");
    private final String message;
    private final String code;

    ErrorEnum(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

}
