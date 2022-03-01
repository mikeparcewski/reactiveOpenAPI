package com.wickedagile.apis.reference.reactoropenapi.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@TestConfiguration
public class EventsConfig {

  public static int deletes, inserts, updates, reads;

  /**
   * counts operations for validation
   * @param de the operation and entity to be processed
   */
  @EventListener
  @Async
  public void handleEntityChange(DomainEvent de) {
    switch(de.getOperation()) {
      case DELETE:
        deletes++;
        break;
      case INSERT:
        inserts++;
        break;
      case UPDATE:
        updates++;
      case READ:
        reads++;
        break;
    }
    log.info("Faking message: {}", de);
  }

}
