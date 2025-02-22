  package com.comparus.service.controller;

  import com.comparus.service.exception.DataSourceAccessException;
  import com.comparus.service.exception.ErrorResponse;
  import io.swagger.v3.oas.annotations.Hidden;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
  import org.springframework.web.bind.annotation.ExceptionHandler;
  import org.springframework.web.bind.annotation.RestControllerAdvice;


  @Hidden
  @RestControllerAdvice
  public class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
      ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), null);
      return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DataSourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataSourceAccessException(DataSourceAccessException ex) {
      ErrorResponse errorResponse = new ErrorResponse(
          "Data source error: " + ex.getDataSourceName(),
          ex.getMessage()
      );
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Throwable ex) {
      ErrorResponse errorResponse = new ErrorResponse("Internal server error", null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }
