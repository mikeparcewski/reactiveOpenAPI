package com.accenture.cloudnative.reference.reactoropenapi.domain.exception;

import com.accenture.cloudnative.reference.reactoropenapi.dto.Error;

/**
 * Simple not found exception
 */
public class NotFoundException extends Exception {

  Error getERROR() {
    return new Error().code("404").message("Not Found");
  }

}
