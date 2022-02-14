package com.wickedagile.apis.reference.reactoropenapi.api;

import com.wickedagile.apis.reference.reactoropenapi.domain.exception.NotFoundException;
import com.wickedagile.apis.reference.reactoropenapi.domain.mapper.VendorMapper;
import com.wickedagile.apis.reference.reactoropenapi.dto.VendorCreateDto;
import com.wickedagile.apis.reference.reactoropenapi.dto.VendorDto;
import com.wickedagile.apis.reference.reactoropenapi.repository.VendorRepository;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Implements the shell created by our OpenAPI code generator.
 */
@Slf4j
@Service
public class VendorAPIDelegateImpl implements VendorApiDelegate {

  @Autowired
  VendorRepository vendorRepository;

  @Autowired
  VendorMapper vendorMapper;

  /**
   * simple vendor lookup by id
   *
   * @param id       the id of the vendor
   * @param exchange an exchange object for us to deal with request/response details
   * @return a matching vendor and @NotFoundException when no matching record is found (caught by InvalidEntityHandler)
   */
  @Override
  public Mono<ResponseEntity<VendorDto>> getVendorById(String id,
                                                        ServerWebExchange exchange) {
    return vendorRepository.findById(id)
        .map(vendor -> {
          log.debug("Retrieved Vendor: {}", vendor.toString());
          return ResponseEntity.status(HttpStatus.OK)
              .contentType(MediaType.APPLICATION_JSON)
              .body(vendorMapper.vendorToVendorDto(vendor));
        })
        .switchIfEmpty(Mono.error(new NotFoundException()));
  }

  /**
   * Adds a vendor to the system
   *
   * @param vendorCreateDto a vendor object for saving
   * @param exchange   an exchange object for us to deal with request/response details
   * @return the created vendor object (or one of the errors caught by InvalidEntityHandler)
   */
  @Override
  public Mono<ResponseEntity<VendorDto>> addVendor(Mono<VendorCreateDto> vendorCreateDto,
                                                   ServerWebExchange exchange) {

    Mono<VendorDto> vendorDto = vendorMapper.vendorCreateDtoToVendorDtoMono(vendorCreateDto);

    return vendorRepository
        .saveAll(vendorMapper.vendorDtoToVendor(vendorDto))
        .doOnError(throwable -> log.error("Trying to create", throwable))
        .next()
        .map(vendor -> {
          log.debug("Saved vendor {}", vendor.toString());
          return ResponseEntity.status(HttpStatus.OK)
              .contentType(MediaType.APPLICATION_JSON)
              .body(vendorMapper.vendorToVendorDto(vendor));
        });
  }

  /**
   * delete a vendor from the system
   *
   * @param id       the id of the vendor to be deleted
   * @param exchange an exchange object for us to deal with request/response details
   * @return the records final state before deletion  (or one of the errors caught by InvalidEntityHandler)
   */
  @Override
  public Mono<ResponseEntity<VendorDto>> deleteVendorById(String id,
                                                            ServerWebExchange exchange) {
    return vendorRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException()))
        .flatMap(vendor -> vendorRepository.delete(vendor).then(Mono.just(vendor)))
        .map(vendor -> vendorMapper.vendorToVendorDto(vendor))
        .map(vendorDto -> ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(vendorDto));
  }

  /**
   * update a vendor record
   *
   * @param id         the id of the vendor to be updated
   * @param vendorDto vendor details that need to changed
   * @param exchange   an exchange object for us to deal with request/response details
   * @return the records most up to date state (or one of the errors caught by InvalidEntityHandler)
   */
  @Override
  public Mono<ResponseEntity<VendorDto>> updateVendorById(String id,
                                                            @Valid Mono<VendorDto> vendorDto,
                                                            ServerWebExchange exchange) {

    return vendorRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException()))
        .flatMap(vendor -> vendorMapper
            .vendorDtoToVendor(vendorDto)
            .map(vendor1 -> vendorMapper.updateVendor(vendor, vendor1)))
        .map(vendor -> vendorRepository.save(vendor))
        .flatMap(vendorMono1 -> vendorMapper.vendorToVendorDto(vendorMono1))
        .map(vendorDtoMono -> ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(vendorDtoMono));
  }

}
