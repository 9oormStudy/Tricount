package clonecoding.tricount.controller;

import clonecoding.tricount.exception.AccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExController {

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<Map<String, Object>> accessExHandle(AccessException e) {
        log.error("[AccessException] ", e);
        Map<String, Object> errorMessage = new HashMap<>();
        errorMessage.put("status", 500);
        errorMessage.put("message", e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
