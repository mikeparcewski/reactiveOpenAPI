package com.wickedagile.apis.reference.reactoropenapi.repository;

import com.wickedagile.apis.reference.reactoropenapi.domain.Vendor;
import com.wickedagile.apis.reference.reactoropenapi.domain.mapper.VendorMapper;
import com.wickedagile.apis.reference.reactoropenapi.event.DomainEvent;
import com.wickedagile.apis.reference.reactoropenapi.event.DomainOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.json.Converter;
import org.bson.json.JsonWriterSettings;
import org.bson.json.StrictJsonWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

/**
 * listens for changes in the data repository and publishes messages
 * based on what happened. Preference would be to use DomainEvents for this bit,
 * but seems to be a bug with Lombok, ReactiveCrudRepositories and DomainEvents
 * that causes a "java.lang.IllegalArgumentException: object is not an instance of declaring class"
 * so we hack it and hardcode the mongo piece.
 * Other databases/repositories have same accessors, so it's a small code change in
 * most cases and a couple of lines of config to change
 */
@Slf4j
@Component
public class RepositoryListener
    extends AbstractMongoEventListener<Vendor> {

  static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"));

  @Autowired
  VendorMapper vendorMapper;

  @Autowired
  ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  ObjectMapper objectMapper;

  /**
   * handle the save event
   *
   * @param event the actual save event
   */
  @Override
  public void onAfterSave(AfterSaveEvent<Vendor> event) {
    if (event != null && event.getDocument() == null) return;
    DomainOperation op =
        event.getDocument().get("_id") == null ?
            DomainOperation.INSERT : DomainOperation.UPDATE;
    pushEvent(event.getSource(), op);
  }

  /**
   * handle the load event
   *
   * @param event the actual load event
   */
  @Override
  public void onAfterLoad(AfterLoadEvent<Vendor> event) {
    pushEvent(getVendor(event.getSource()), DomainOperation.READ);
  }

  /**
   * handle the delete event
   *
   * @param event the actual delete event
   */
  @Override
  public void onAfterDelete(AfterDeleteEvent<Vendor> event) {
    pushEvent(getVendor(event.getSource()), DomainOperation.DELETE);
  }

  /***
   * loads the vendor object up from document
   * This isn't a great practice (from a performance
   * standpoint) and only exists for the demo.
   * @param doc the vendor bson document
   * @return the converter Vendor object
   */
  Vendor getVendor(Document doc) {
    try {
      return objectMapper.readValue(
        doc.toJson(
          JsonWriterSettings.builder()
              .dateTimeConverter(new JsonDateTimeConverter())
              .build()
        ),
      Vendor.class);
    } catch (Exception e) {
      log.error("Processing vendor event", e);
    }
    return null;
  }

  /**
   * produce the actual notification event
   *
   * @param vendor   the vendor involved with the event
   * @param operation it's operation
   */
  void pushEvent(Vendor vendor, DomainOperation operation) {
    DomainEvent de = new DomainEvent(operation, vendor);
    applicationEventPublisher.publishEvent(de);
  }


  /**
   * class to handle date conversions from the Bson document
   */
  static class JsonDateTimeConverter implements Converter<Long> {

    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_INSTANT
        .withZone(ZoneId.of("UTC"));

    @Override
    public void convert(Long value, StrictJsonWriter writer) {
      try {
        Instant instant = new Date(value).toInstant();
        String s = DATE_TIME_FORMATTER.format(instant);
        writer.writeString(s);
      } catch (Exception e) {
        log.error(String.format("Fail to convert offset %d to JSON date", value), e);
      }
    }
  }


}
