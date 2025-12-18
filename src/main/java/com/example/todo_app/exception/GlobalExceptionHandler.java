package com.example.todo_app.exception;

import com.example.todo_app.exception.dto.GlobalExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalExceptionDto> handleEverything() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GlobalExceptionDto("ERROR", "Ошибка сервера"));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<GlobalExceptionDto> NoSuchElementException(NoSuchElementException ex) {
        GlobalExceptionDto error = new GlobalExceptionDto(
                "NOT_FOUND",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalExceptionDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        GlobalExceptionDto error = new GlobalExceptionDto(
                "BAD_REQUEST",
                "Невалидный JSON формат данных"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}