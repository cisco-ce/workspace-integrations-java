package com.cisco.workspaceintegrations.common.jwt;

public class JWTProcessingException extends JWTException {

    public JWTProcessingException(String message) {
        super(message);
    }

    public JWTProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

}
