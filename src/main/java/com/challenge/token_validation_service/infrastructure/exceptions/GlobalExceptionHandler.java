package com.challenge.token_validation_service.infrastructure.exceptions;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Map<String, String>> handleMissingParams(
      MissingServletRequestParameterException ex) {
    log.warn("Parâmetro obrigatório ausente: {}", ex.getParameterName());
    Map<String, String> response = new HashMap<>();
    response.put("error", "Parâmetro 'token' é obrigatório");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
    log.error("Erro interno no servidor: {}", ex.getMessage(), ex);
    Map<String, String> response = new HashMap<>();
    response.put("error", "Erro interno no servidor");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
