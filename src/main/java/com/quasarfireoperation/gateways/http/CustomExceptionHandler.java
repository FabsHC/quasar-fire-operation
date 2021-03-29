package com.quasarfireoperation.gateways.http;

import com.quasarfireoperation.domains.exception.SatelliteNotFoundException;
import com.quasarfireoperation.gateways.http.response.ErrorResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public HttpEntity<ErrorResponse> handlerMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
    final ErrorResponse errorResponse = this.processFieldErrors(ex.getBindingResult().getFieldErrors());
    final HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
    return new ResponseEntity(errorResponse, responseHeaders, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({SatelliteNotFoundException.class})
  public HttpEntity<ErrorResponse> handlerSatelliteNotFoundException(final SatelliteNotFoundException ex) {
    final HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.getError().add(ex.getMessage());
    return new ResponseEntity(errorResponse, responseHeaders, HttpStatus.NOT_FOUND);
  }

  private ErrorResponse processFieldErrors(final List<FieldError> fieldErrors) {
    final ErrorResponse errorResponse = new ErrorResponse();
    fieldErrors.forEach(fieldError -> {
      String localizedErrorMessage = fieldError.getDefaultMessage();
      errorResponse.getError().add(fieldError.getField().concat(":").concat(localizedErrorMessage));
    });
    return errorResponse;
  }
}