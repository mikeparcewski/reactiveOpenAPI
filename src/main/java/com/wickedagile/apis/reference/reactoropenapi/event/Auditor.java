package com.wickedagile.apis.reference.reactoropenapi.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * just here to show multiple folks can subscribe
 * to the application events
 */
@Slf4j
@Component
public class Auditor {

  /**
   * catches a entity change event and processes it
   *
   * @param de the actual domain event
   */
  @Async
  @EventListener
  public void handleEntityChange(DomainEvent de) {
    log.info("AUDITING CHANGE: {}", de);
  }

}