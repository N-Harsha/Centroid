package com.example.centroid.utils;

public enum SuccessEnum {

    USER_REGISTRATION_SUCCESSFUL("User registration successful"),
    USER_REQUEST_ACCEPTED_SUCCESSFULLY("User Request accepted successful"),
    USER_REQUEST_REJECTED_SUCCESSFULLY("User Request rejected successful"),
    USER_REQUEST_CANCELED_SUCCESSFULLY("User Request canceled successful"),
    USER_REQUEST_SENT_SUCCESSFULLY("User Request sent successfully");
    private final String message;

    SuccessEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
