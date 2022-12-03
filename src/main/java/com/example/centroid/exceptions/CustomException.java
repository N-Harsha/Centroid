package com.example.centroid.exceptions;

import com.example.centroid.model.Dto.ApiError;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{
    private static final long serialVersionUID = 1488638787124982982L;
    private ApiError errorResponse;

    public CustomException(){super();}

    public CustomException(String message){super(message);}

    public CustomException(Throwable cause){super(cause);}

    public CustomException(String message,Throwable cause){super(message, cause);}

    public CustomException(ApiError errorResponse){
        this.errorResponse=errorResponse;
    }

    public CustomException(String message, HttpStatus status){super(message);}

    public ApiError getErrorResponse(){return errorResponse;}

    public void setErrorResponse(ApiError errorResponse) {
        this.errorResponse = errorResponse;
    }
}
