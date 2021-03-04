package com.jszipcoders.moneymanager.models;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

    private final String message;

    public ErrorResponse(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
