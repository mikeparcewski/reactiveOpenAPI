package com.wickedagile.apis.reference.reactoropenapi.domain.exception;

import com.wickedagile.apis.reference.reactoropenapi.domain.mapper.ErrorMapper;
import com.wickedagile.apis.reference.reactoropenapi.dto.Error;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

/**
 * Centralized management of  controller errors to standardize
 * the error contract defined by the OpenAPI spec
 */
@Slf4j
@ControllerAdvice
public class InvalidEntityHandler {

  static final String INVALID_PAYLOAD = "Invalid Payload";
  static final String DATA_EXCEPTION = "Data Exception";
  @Autowired
  ErrorMapper errorMapper;

  /**
   * catches an exception when processing an entity and returns details around issues with the payload
   *
   * @param ex the WebExchangeBindException that was thrown
   * @return an error response with details on the problems with the payload
   */
  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity handleBindException(WebExchangeBindException ex) {
    log.error("Trying to process (WEBE)", ex);
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
        .contentType(MediaType.APPLICATION_JSON)
        .body(errorMapper.fieldErrorsToErrors(ex.getFieldErrors()));
  }

  /**
   * catches field validation issues at a different level than WebExchangeBindException and returns details around the issues
   *
   * @param ex the IllegalArgumentException that was thrown
   * @return an error response with details on the problems with the payload
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity handleArgException(IllegalArgumentException ex) {
    log.error("Processing request (IAE)", ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Arrays.asList(errorMapper.argErrorToError(ex).code(INVALID_PAYLOAD)));
  }

  /**
   * catches exceptions caused by data operations (e.g. duplicate key) and returns details around the issues
   *
   * @param ex the DataAccessException that was thrown
   * @return an error response with details on the problems thrown by the data source
   */
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity handleDataException(DataAccessException ex) {
    log.error("Processing request (DAE)", ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Arrays.asList(errorMapper.dataErrorToError(ex).code(DATA_EXCEPTION)));
  }

  /**
   * catches exceptions when an entity isn't found for an operation
   *
   * @param ex the NotFoundException that was thrown
   * @return a not found response
   */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity handleNotFoundException(NotFoundException ex) {
    log.error("Processing request (NFE)", ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Arrays.asList(ex.getERROR()));
  }

  /**
   * catches exceptions when mandatory fields aren't found in the payload
   *
   * @param ex the ServerWebInputException that was thrown
   * @return a response that tries to provide most specific error message
   */
  @ExceptionHandler(ServerWebInputException.class)
  public ResponseEntity handleSWIException(ServerWebInputException ex) {
    log.error("Processing request (SWIE)", ex);
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Arrays.asList(
            new Error().code("Invalid JSON").message(ex.getMostSpecificCause().getMessage())));
  }

}
