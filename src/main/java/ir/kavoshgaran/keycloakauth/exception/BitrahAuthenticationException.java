package ir.kavoshgaran.keycloakauth.exception;

import org.springframework.http.HttpStatus;

public class BitrahAuthenticationException extends BitrahBaseException {
    private HttpStatus httpStatus;

    public BitrahAuthenticationException(String message) {
        super(message);
    }

    public BitrahAuthenticationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
