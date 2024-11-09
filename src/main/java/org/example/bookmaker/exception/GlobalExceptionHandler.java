package org.example.bookmaker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BookNotFoundException.class)
    ProblemDetail handleBookmarkNotFoundException(BookNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Bookmark not found");
        problemDetail.setType(URI.create("https://api.bookmarks.com/errors/not-found"));
        problemDetail.setProperty("errorCategory", "Generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorResult errorResult = new ErrorResult();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResult.getFieldErrors().put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);

    }


    @Getter
    @NoArgsConstructor
    static class ErrorResult {
        private final Map<String, String> fieldErrors = new HashMap<>();

        ErrorResult(String field, String message) {
            this.fieldErrors.put(field, message);
        }
    }
//
//    @Getter
//    @AllArgsConstructor
//    static class FieldValidationErrors {
//        private String field;
//        private String message;
//    }
}
