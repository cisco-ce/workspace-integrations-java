package com.cisco.workspaceintegrations.common.jwt;

public class JWTInvalidException extends JWTException {

    public JWTInvalidException(String message) {
        super(message);
    }

    public JWTInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}
