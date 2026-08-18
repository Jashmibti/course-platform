package com.learningplatform.enrollment.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EnrollmentAlreadyExistsException.class)
    ResponseEntity<Map<String,Object>> duplicate(EnrollmentAlreadyExistsException e){return build(HttpStatus.CONFLICT,e.getMessage());}
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<Map<String,Object>> notFound(ResourceNotFoundException e){return build(HttpStatus.NOT_FOUND,e.getMessage());}
    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<Map<String,Object>> state(IllegalStateException e){return build(HttpStatus.CONFLICT,e.getMessage());}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException e){
        Map<String,Object> r=new LinkedHashMap<>(); r.put("timestamp",LocalDateTime.now());r.put("status",400);r.put("error","Validation failed");
        Map<String,String> d=new LinkedHashMap<>();e.getBindingResult().getFieldErrors().forEach(x->d.put(x.getField(),x.getDefaultMessage()));r.put("details",d);
        return ResponseEntity.badRequest().body(r);
    }
    private ResponseEntity<Map<String,Object>> build(HttpStatus s,String m){
        Map<String,Object> r=new LinkedHashMap<>();r.put("timestamp",LocalDateTime.now());r.put("status",s.value());r.put("error",s.getReasonPhrase());r.put("message",m);return ResponseEntity.status(s).body(r);
    }
}
