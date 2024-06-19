package org.dwrik.department.controller;

import org.dwrik.department.exception.DepartmentAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class DepartmentControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationException(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, Object>();
        for (var error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DepartmentAlreadyExistsException.class)
    public Map<String, Object> handleDepartmentAlreadyExistsException(DepartmentAlreadyExistsException ex) {
        return Map.of("error", ex.getMessage());
    }
}
