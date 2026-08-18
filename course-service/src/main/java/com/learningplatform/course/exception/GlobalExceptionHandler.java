package com.learningplatform.course.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ============================================================
    // RESOURCE NOT FOUND
    // ============================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiError> notFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return build(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    // ============================================================
    // VALIDATION ERROR
    // ============================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getField)
                .distinct()
                .collect(Collectors.joining(
                        ", ",
                        "Invalid fields: ",
                        ""
                ));

        return build(
                HttpStatus.BAD_REQUEST,
                message,
                request
        );
    }

    // ============================================================
    // BAD REQUEST
    // ============================================================

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiError> badRequest(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return build(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }

    // ============================================================
    // GENERIC EXCEPTION
    //
    // TEMPORARILY SHOW THE REAL ERROR WHILE DEBUGGING
    // ============================================================

    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String, Object>> generic(
            Exception ex,
            HttpServletRequest request) {

        // Print complete exception and stack trace
        // to the course-service Docker logs.
        ex.printStackTrace();

        Map<String, Object> body = new HashMap<>();

        body.put(
                "timestamp",
                OffsetDateTime.now()
        );

        body.put(
                "status",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        body.put(
                "error",
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        );

        body.put(
                "message",
                ex.getMessage()
        );

        body.put(
                "exception",
                ex.getClass().getName()
        );

        body.put(
                "path",
                request.getRequestURI()
        );

        // Include the immediate cause if available
        if (ex.getCause() != null) {

            body.put(
                    "cause",
                    ex.getCause().getMessage()
            );

            body.put(
                    "causeException",
                    ex.getCause().getClass().getName()
            );
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

    // ============================================================
    // BUILD API ERROR
    // ============================================================

    private ResponseEntity<ApiError> build(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        return ResponseEntity
                .status(status)
                .body(
                        new ApiError(
                                OffsetDateTime.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                message,
                                request.getRequestURI()
                        )
                );
    }
}