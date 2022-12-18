package com.example.centroid.utils;

public enum ErrorEnum {
    USER_SESSION_EXPIRED("Session expired, sign in again", "ERR101"),

    BAD_CREDENTIALS("Bad credentials, please try again", "ERR102"),
    DUPLICATE_USERNAME("the username is already in use,choose another one", "ERR103"),
    DUPLICATE_EMAIL("the email is already in use,choose another one", "ERR104"),
    VERIFY_EMAIL_MISMATCH("the email and verify email do not match", "ERR105"),
    VERIFY_PASSWORD_MISMATCH("the password and verify password do not match", "ERR106"),
    INVALID_USERNAME("the invalid username, please enter a valid username", "ERR107"),
    INVALID_PASSWORD("the invalid password, please enter a valid password", "ERR108"),
    INVALID_FIRST_NAME("the invalid first name, please enter a valid first name", "ERR109"),
    INVALID_LAST_NAME("the invalid last name, please enter a valid last name", "ERR110"),
    INVALID_EMAIL("the invalid email, please enter a valid email", "ERR111"),
    USER_SESSION_CREATION_FAILED("Unable to create user session while logging", "ERR112"),
    INVALID_USER_REQUEST("User request is not possible", "ERR113"),
    USER_REQUEST_NOT_FOUND("User request is not present", "ERR114"),
    UNAUTHORIZED_USER_REQUEST_UPDATE("user is not authorized to update this user request", "ERR115"),
    USER_REQUEST_ALREADY_UPDATED("user request is no longer has pending status", "ERR116"),
    USER_NOT_MEMBER_OF_CONVERSATION("user is not a member of Conversation", "ERR117"),
    CONVERSATION_DOSE_NOT_EXIST("conversation does not exist", "ERR118" ),
    USER_REQUEST_IN_PENDING_STATUS("User request is in pending status","ERR119"),
    USER_REQUEST_IN_Accepted_STATUS("Already friends...","ERR120");

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
