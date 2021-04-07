package com.accenture.cloudnative.reference.reactoropenapi.repository;

import com.accenture.cloudnative.reference.reactoropenapi.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * simple repo for the entity, has most things you'll
 * need like find/save/update/delete/etc...
 */
public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {

}
