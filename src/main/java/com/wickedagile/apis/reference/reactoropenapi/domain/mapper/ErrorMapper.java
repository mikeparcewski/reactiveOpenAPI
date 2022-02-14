package com.wickedagile.apis.reference.reactoropenapi.domain.mapper;

import com.wickedagile.apis.reference.reactoropenapi.dto.Error;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.FieldError;

/**
 * Mapstruct based implementation to support mapping from exception errors (Generated by Spring)
 * to Models.  NOTE: We separate these so we can have a consistent error interface for our contracts
 */
@Mapper(componentModel = "spring")
public interface ErrorMapper {

  /**
   * maps a FieldError to Error object
   *
   * @param error the FieldError object to be mapped
   * @return the resulting error object
   */
  @Mappings({
      @Mapping(target = "code", source = "field"),
      @Mapping(target = "message", source = "defaultMessage")
  })
  Error fieldErrorToError(FieldError error);

  /**
   * maps a list of field errors to our error contract object
   *
   * @param errors the list of FieldError(s) to convert
   * @return a list of Error objects to match our contract
   */
  List<Error> fieldErrorsToErrors(List<FieldError> errors);

  /**
   * maps an IllegalArgumentException to our error contract
   *
   * @param exception the IllegalArgumentException to be mapped
   * @return the resulting Error object
   */
  Error argErrorToError(IllegalArgumentException exception);

  /**
   * maps an DataAccessException to our error contract
   *
   * @param exception the DataAccessException to be mapped
   * @return the resulting Error object
   */
  Error dataErrorToError(DataAccessException exception);

}