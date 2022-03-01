package com.wickedagile.apis.reference.reactoropenapi.repository;

import com.wickedagile.apis.reference.reactoropenapi.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * simple repo for the entity, has most things you'll
 * need like find/save/update/delete/etc...
 */
@Repository
public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {

}
