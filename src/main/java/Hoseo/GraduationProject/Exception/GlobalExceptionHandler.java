package Hoseo.GraduationProject.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity handleBusinessLogicExceptions(BusinessLogicException e) {
        return ResponseEntity.status(e.getExceptionType().getErrorCode())
                .body(ErrorResponse.of(e.getExceptionType()));
    }
}
