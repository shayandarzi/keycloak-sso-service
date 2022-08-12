package ir.kavoshgaran.keycloakauth.dto.rest;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class BitrahError implements Serializable {
    private String message;
    private HttpStatus httpStatus;

    public BitrahError() {
    }

    public BitrahError(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
