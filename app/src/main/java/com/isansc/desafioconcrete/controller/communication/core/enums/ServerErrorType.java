package com.isansc.desafioconcrete.controller.communication.core.enums;

/**
 * Created by Isan on 01-Nov-17.
 */

public enum ServerErrorType {
    UNAUTHORIZED("UNAUTHORIZED"),
    INVALID_PARAMETERS("INVALID_PARAMETERS"),
    INTERNAL_ERROR("INTERNAL_ERROR"),
    UNKNOWN_ERROR("UNKNOWN_ERROR"),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS");

    private String stringValue;

    ServerErrorType(String toString) {
        stringValue = toString;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
