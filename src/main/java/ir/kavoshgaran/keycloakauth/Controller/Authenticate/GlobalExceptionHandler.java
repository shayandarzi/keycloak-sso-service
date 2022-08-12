package ir.kavoshgaran.keycloakauth.Controller.Authenticate;

import ir.kavoshgaran.keycloakauth.dto.rest.BitrahError;
import ir.kavoshgaran.keycloakauth.exception.BitrahAuthenticationException;
import ir.kavoshgaran.keycloakauth.exception.BitrahBaseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({BitrahBaseException.class, BitrahAuthenticationException.class})
    public ResponseEntity<Object> handleExc(BitrahAuthenticationException exception) {
        BitrahError bitrahError = new BitrahError(exception.getMessage(), exception.getHttpStatus());
        return ResponseEntity.status(exception.getHttpStatus())
                .header("Content-Type", "application/json; charset=utf-8")
                .body(bitrahError);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleExc(Exception e) {
        BitrahError bitrahError = new BitrahError("Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Content-Type", "application/json; charset=utf-8")
                .body(bitrahError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BitrahError bitrahError = new BitrahError();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        Map<String, Object> errors = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        bitrahError.setMessage(errors.toString());
        bitrahError.setHttpStatus(status);
        return ResponseEntity.status(status).body(bitrahError);
    }
}
