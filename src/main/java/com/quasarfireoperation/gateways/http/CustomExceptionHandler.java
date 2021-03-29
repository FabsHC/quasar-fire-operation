package com.quasarfireoperation.gateways.http;

import com.quasarfireoperation.domains.exception.DecryptMessageException;
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public HttpEntity<ErrorResponse> handlerMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
    final ErrorResponse errorResponse = this.processFieldErrors(ex.getBindingResult().getFieldErrors());
    return new ResponseEntity(errorResponse, buildHttpHeader(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({SatelliteNotFoundException.class})
  public HttpEntity<ErrorResponse> handlerSatelliteNotFoundException(final SatelliteNotFoundException ex) {
    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.addError(ex.getMessage());
    return new ResponseEntity(errorResponse, buildHttpHeader(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({DecryptMessageException.class})
  public HttpEntity<ErrorResponse> handlerDecryptMessageException(final DecryptMessageException ex) {
    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.addError(ex.getMessage());
    return new ResponseEntity(errorResponse, buildHttpHeader(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Throwable.class)
  public HttpEntity<ErrorResponse> handleThrowable(final Throwable ex) {
    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.addError(isNotBlank(ex.getMessage()) ? ex.getMessage():ex.getLocalizedMessage());
    return new ResponseEntity(errorResponse, buildHttpHeader(), INTERNAL_SERVER_ERROR);
  }

  private HttpHeaders buildHttpHeader() {
    final HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
    return responseHeaders;
  }

  private ErrorResponse processFieldErrors(final List<FieldError> fieldErrors) {
    final ErrorResponse errorResponse = new ErrorResponse();
    fieldErrors.forEach(fieldError -> {
      String localizedErrorMessage = fieldError.getDefaultMessage();
      errorResponse.addError(fieldError.getField().concat(":").concat(localizedErrorMessage));
    });
    return errorResponse;
  }
}